package co.vgetto.har.audio;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import co.vgetto.har.MyApplication;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.SavedFile;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.rxservices.RxHistoryService;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.syncadapter.SyncObserver;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Kovje on 11.8.2015..
 */

//service that is started when recording audio
public class RecordAudioService extends Service {
  @Inject RxScheduleService rxScheduleService;
  @Inject RxHistoryService rxHistoryService;
  @Inject ContentResolver resolver;
  @Inject SyncObserver observer;

  private Subscription recordingSubscription;
  private PowerManager.WakeLock wakeLock;
  private RecordingConfiguration recordingConfiguration;
  private UploadConfiguration uploadConfiguration;

  private int type;
  private long foreignId;

  public RecordAudioService() {

  }

  /**
   * either a trigger or alarm started the service, time to record!
   * get recording and upload configuration for this recording session from schedule/trigger table,
   * subscribe to observable chain on new thread!
   */
  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RecordAudioService");
    wakeLock.acquire();
    MyApplication.get(this).getAppComponent().inject(this);

    Timber.i("HELLO FROM ON START COMMAND -> " + this.toString());

    Bundle b = intent.getExtras();

    type = b.getInt("type", -1);

    // base on type, get schedule/trigger from bundle, and extracts (foreign) id,
    // recording and upload conf.
    if (type == History.HISTORY_TYPE_SCHEDULE) {
      Schedule schedule = b.getParcelable("schedule");
      foreignId = schedule.id();
      recordingConfiguration = schedule.scheduleConfiguration().recordingConfiguration();
      uploadConfiguration = schedule.scheduleConfiguration().uploadConfiguration();
    } else {
      Trigger trigger = b.getParcelable("trigger");
      foreignId = trigger.id();
      recordingConfiguration = trigger.triggerConfiguration().recordingConfiguration();
      uploadConfiguration = trigger.triggerConfiguration().uploadConfiguration();
    }

    recordingSubscription = startRecordingWhenPossible(recordingConfiguration);

    /*
    RxAudioRecorder audioRecorder = new RxAudioRecorder(recordingConfiguration);

    recordingSubscription = audioRecorder.start().finallyDo(() -> stopSelf()).map(
        onFileRecoded) // handle when one file is recorded
        .doOnSubscribe(onRecordingStarted) // create history for recording session
        .doOnCompleted(onRecordingCompleted) // when recording session end
        .doOnError(onRecordingError) // if error occured during recording session
        .subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.io())
        .subscribe(s -> Timber.i("Audio file saved ->  " + s),
            e -> Timber.i("Error " + e.getMessage()),
            () -> Timber.i("Audio recording ended successfully"));
*/
    return START_NOT_STICKY;

  }

  public Subscription startRecordingWhenPossible(RecordingConfiguration recordingConfiguration) {
    RxAudioRecorder rxAudioRecorder = new RxAudioRecorder(recordingConfiguration);

    return rxAudioRecorder.start().finallyDo(() -> stopSelf()).map(
        onFileRecoded) // handle when one file is recorded
        .doOnSubscribe(onRecordingStarted) // create history for recording session
        .doOnCompleted(onRecordingCompleted) // when recording session end
        .doOnError(onRecordingError) // if error occured during recording session
        .subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.io())
        .subscribe(s -> Timber.i("Audio file saved ->  " + s),
            e -> Timber.i("Error " + e.getMessage()),
            () -> Timber.i("Audio recording ended successfully"));

  }

  /**
   * if error occured, write to history that the error occured and current time
   */
  public final Action1<Throwable> onRecordingError = new Action1<Throwable>() {
    @Override public void call(Throwable throwable) {
      ContentValues values = new History.ContentValuesBuilder().foreignId(foreignId)
          .type(type)
          .state(History.HISTORY_STATE_ERROR_WHEN_RECORDING)
          .endedRecordingDate(System.currentTimeMillis())
          .build();
      rxHistoryService.editHistoryByForeignId(values).toBlocking().first();
    }
  };

  /**
   * when recording session completes, change the state in history to SUCCESSFUL,
   * and set the time of completion
   */
  public final Action0 onRecordingCompleted = new Action0() {
    @Override public void call() {
      ContentValues values = new History.ContentValuesBuilder().foreignId(foreignId)
          .type(type)
          .state(History.HISTORY_STATE_SUCCESSFUL)
          .endedRecordingDate(System.currentTimeMillis())
          .build();
      rxHistoryService.editHistoryByForeignId(values).toBlocking().first();
    }
  };

  /**
   * before recording starts, create a history in db for this recording session
   */
  public final Action0 onRecordingStarted = new Action0() {
    @Override public void call() {
      ContentValues history = new History.ContentValuesBuilder().foreignId(foreignId)
          .type(type)
          .state(History.HISTORY_STATE_RECORDING)
          .startedRecordingDate(System.currentTimeMillis())
          .savedFiles(null)
          .build();

      // create entry in history table, block on it
      rxHistoryService.insertHistory(history).toBlocking().first();

      // if recording session started by schedule, change state of schedule
      if (type == History.HISTORY_TYPE_SCHEDULE) {
        rxScheduleService.setScheduleState(foreignId, Schedule.SCHEDULE_STATE_STARTED)
            .toBlocking()
            .first();
      }

      Timber.i("History created..");
    }
  };

  /**
   * Update history for this recording session with the file just recorded
   */
  public final Func1<String, String> onFileRecoded = new Func1<String, String>() {
    @Override public String call(String recordedFile) {
      History history;
      List<SavedFile> savedFiles;

      // query history for this schedule/trigger
      if (type == History.HISTORY_TYPE_SCHEDULE) {
        history = rxHistoryService.getHistoryByForeignIdAndType(foreignId, type).toBlocking().first();
      } else {
        history = rxHistoryService.getHistoryByForeignIdAndType(foreignId, type).toBlocking().first();
      }

      savedFiles = history.savedFiles();
      savedFiles.add(SavedFile.create(recordedFile, System.currentTimeMillis(), false));

      ContentValues updatedHistory = new History.ContentValuesBuilder().id(history.id())
          .savedFiles(savedFiles)
          .build();

      rxHistoryService.editHistory(updatedHistory).toBlocking().first();

      if (uploadConfiguration.dropboxUpload()) {
        Timber.i("Uploading to dropbox...");
      }

      return recordedFile;
    }
  };

  @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onDestroy() {
    Timber.i("Releasing wake lock...");
    if (recordingSubscription != null) {
      recordingSubscription.unsubscribe();
    }
    wakeLock.release();
    super.onDestroy();
  }
}
