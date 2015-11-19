package co.vgetto.har.audio;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import co.vgetto.har.MyApplication;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.SavedFile;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.rxservices.RxHistoryService;
import co.vgetto.har.rxservices.RxNotificationService;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.rxservices.RxUserService;
import co.vgetto.har.syncadapter.SyncObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by Kovje on 11.8.2015..
 */
public class RxAudioRecorder {
  @Inject RxHistoryService rxHistoryService;
  @Inject RxScheduleService rxScheduleService;
  @Inject RxNotificationService rxNotificationService;
  @Inject RxUserService rxUserService;
  @Inject SyncObserver syncObserver;

  private final PublishSubject<Boolean> iTalkToRecordAudioService;
  private PublishSubject<Integer> queueSubject;

  private MediaRecorder mediaRecorder = null;
  private List<IntentData> intentDataQueue = new ArrayList<>();
  private Subscription recordingSessionSubscription;

  /**
   * subscribes to queueSubject immediately,
   * when a recording session observable emits onError() or onCompleted()
   * queueSubject  emits an int values, either -1 for onError() or 1 for onCompleted()
   * observer handles a new recording session if a schedule/trigger started RecordAudioService
   * while already a recording session was ongoing, or stops RecordAudioService
   */
  public RxAudioRecorder(Context context, PublishSubject<Boolean> iTalkToRecordAudioService) {
    MyApplication.get(context).getAppComponent().inject(this);
    this.iTalkToRecordAudioService = iTalkToRecordAudioService;
    this.queueSubject = PublishSubject.create();

    queueSubject.subscribe(integer -> {
      if (intentDataQueue.size() > 0) {
        // if there is new data in the queue, start a new recording session
        if (!recordingSessionSubscription.isUnsubscribed()) {
          recordingSessionSubscription.unsubscribe();
        }
        Timber.i("Starting a new recording session..");
        recordingSessionSubscription = startRecordingSession(getNextFromQueue());
      } else {
        // no new intent data in queue, stop the service
        Timber.i("Stopping service..");
        this.iTalkToRecordAudioService.onNext(true);
      }
    });
  }

  /**
   * only public method, starts a recording session with intentData
   * or if a recording session is ongoing, wait in line
   * todo add check from uploadConfiguration to wait, or to cancel recording session
   * todo add that to uploadCOnfiguration and rename it to savingAndNotifiyingConfiguration
   */
  public void addToQueue(IntentData intentData) {
    intentDataQueue.add(intentData);

    // if not recording already, start a new recording session
    // if recording session is ongoing, queueSubject subscription should handle starting
    // a new recording session
    if (recordingSessionSubscription == null) {
      Timber.i("Starting recording session for the first time..");
      recordingSessionSubscription = startRecordingSession(getNextFromQueue());
    }
  }

  /**
   * returns first (index 0) element from intent data list, and removes it from the list
   */
  private IntentData getNextFromQueue() {
    IntentData data = intentDataQueue.get(0);
    intentDataQueue.remove(data);
    return data;
  }

  /**
   * starts  MediaRecorder and return a file path if successfful
   */
  private String startMediaRecorder(IntentData intentData) {
    String filePath =
        StorageHelper.getAudioFilePath(intentData.recordingConfiguration().folderName(),
            intentData.recordingConfiguration().filePrefix());
    if (mediaRecorder == null) {
      mediaRecorder = new MediaRecorder();
      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
      mediaRecorder.setOutputFile(filePath);
      mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      try {
        mediaRecorder.prepare();
      } catch (IOException e) {
        Timber.i("prepare() failed");
      }

      mediaRecorder.start();
      return filePath;
    }
    return null;
  }

  /**
   * stop and release media recorder
   */
  private void stopMediaRecorder() {
    mediaRecorder.stop();
    mediaRecorder.release();
    mediaRecorder = null;
  }

  /**
   * returns a recording session observable, that will emit a filename of the file, once
   * he is recorded.
   *
   * Observable chain starts with an interval observable, that will emit an item
   * every (duration of recording + delay between recordings) seconds
   *
   * exp. if duration is set to 17s, and the delay is 5s, it will emit an item every 22s
   *
   * the number of files recorded aka. items emitted by this observable is determined by
   * numberOfRecordings from the recording configuration
   *
   * Every time the interval observable emits an item, flatMap will return an Observable
   * that emit's the String of the filename delivered from startMediaRecorder()
   *
   * If filename isn't null ( .filter() ), flatMap will return an Observable that will emit
   * a String that is a filepath to the recorded audio file, it will also stop the media recorder
   * with stopMediaRecorder() .
   *
   * * zip() will emit items (Long and String) once both observables emit an item,
   * Observable.just() emit's an item right away, and timer will emit afer duration() time,
   */
  private Observable<String> getRecordingSessionObservable(IntentData intentData) {
    return Observable.interval(0,
        intentData.recordingConfiguration().delayBetween() + intentData.recordingConfiguration()
            .duration(), TimeUnit.SECONDS)
        // changed flatMap to concatMap, concatMap will always spit out items chronologically
        .concatMap(l -> Observable.just(startMediaRecorder(intentData))
            .filter(fileName -> fileName != null)
            .concatMap(fileName -> Observable.zip(
                Observable.timer(intentData.recordingConfiguration().duration() + 1,
                    TimeUnit.SECONDS), Observable.just(fileName), (Long, String) -> {
                  stopMediaRecorder();
                  return fileName;
                }))).take(intentData.recordingConfiguration().numOfRecordings());
  }

  /**
   * start a recording session, returns a subscription..
   * handles one recording session, emits a an integer value on onError() or
   * onCompleted() through queueSubject
   */
  private Subscription startRecordingSession(IntentData intentData) {
    return getRecordingSessionObservable(intentData).map(
        onFileRecorded(intentData)) // handle when one file is recorded
        .doOnSubscribe(onRecordingStarted(intentData)) // create history for recording session
        .doOnCompleted(onRecordingCompleted(intentData)) // when recording session ends
        .doOnError(onRecordingError(intentData)) // if error occured during recording session
        .subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.io())
        .subscribe(s -> Timber.i("Audio file saved ->  " + s), e -> {
          Timber.i("Error " + e.getMessage());
          queueSubject.onNext(-1); // notify recording session ended with error
        }, () -> {
          Timber.i("Audio recording ended successfully");
          queueSubject.onNext(1); // notify recording session ended successfully
        });
  }

  /**
   * if error occured, write to history that the error occured and current time
   */
  private Action1<Throwable> onRecordingError(IntentData intentData) {
    return throwable -> {
      ContentValues values = new History.ContentValuesBuilder().foreignId(intentData.foreignId())
          .type(intentData.type())
          .state(History.HISTORY_STATE_ERROR_WHEN_RECORDING)
          .endedRecordingDate(System.currentTimeMillis())
          .build();
      rxHistoryService.editHistoryByForeignId(values).toBlocking().first();
    };
  }

  /**
   * when recording session completes, change the state in history to SUCCESSFUL,
   * and set the time of completion
   */
  private Action0 onRecordingCompleted(IntentData intentData) {
    return () -> {
      ContentValues values = new History.ContentValuesBuilder().foreignId(intentData.foreignId())
          .type(intentData.type())
          .state(History.HISTORY_STATE_SUCCESSFUL)
          .endedRecordingDate(System.currentTimeMillis())
          .build();

      rxHistoryService.editHistoryByForeignId(values).toBlocking().first();

      // if user has chosen to notify on start recording, notify him
      if (intentData.uploadConfiguration().notifyOnEndRecording()) {
        String msg;
        if (intentData.type() == History.HISTORY_TYPE_SCHEDULE) {
          msg = "Schedule stopped recording";
        } else {
          msg = "Trigger stopped recording";
        }
        rxNotificationService.newNotification((int) intentData.foreignId(), "Notification", msg);
      }

    };
  }

  /**
   * before recording starts, create a history in db for this recording session
   */
  private Action0 onRecordingStarted(IntentData intentData) {
    return () -> {
      Timber.i("Starting new recording session");
      long historyId;
      // TODO check if trigger or schedule
      // if trigger check if history already exist, edit other method also

      if (intentData.type() == History.HISTORY_TYPE_SCHEDULE) {
        ContentValues history = new History.ContentValuesBuilder().foreignId(intentData.foreignId())
            .type(intentData.type())
            .state(History.HISTORY_STATE_RECORDING)
            .startedRecordingDate(System.currentTimeMillis())
            .savedFiles(null)
            .build();

        // create entry in history table, block on it
        historyId = rxHistoryService.insertHistory(history).toBlocking().first();

        // if recording session started by schedule, change state of schedule
        rxScheduleService.setScheduleState(intentData.foreignId(), Schedule.SCHEDULE_STATE_STARTED)
            .toBlocking()
            .first();
      } else {
        // if recording started by trigger, check if history for this trigger already exists, if not, create it
        History h =
            rxHistoryService.getHistoryByForeignIdAndType(intentData.foreignId(), intentData.type())
                .toBlocking()
                .first();

        if (h == null) {
          ContentValues history =
              new History.ContentValuesBuilder().foreignId(intentData.foreignId())
                  .type(intentData.type())
                  .state(History.HISTORY_STATE_RECORDING)
                  .startedRecordingDate(System.currentTimeMillis())
                  .savedFiles(null)
                  .build();

          // create entry in history table, block on it
          historyId = rxHistoryService.insertHistory(history).toBlocking().first();
        } else {
          historyId = h.id();
        }
      }

      // if user has chosen to notify on start recording, notify him
      if (intentData.uploadConfiguration().notifyOnStartRecording()) {
        String msg;
        if (intentData.type() == History.HISTORY_TYPE_SCHEDULE) {
          msg = "Schedule started recording";
        } else {
          msg = "Trigger started recording";
        }
        rxNotificationService.newNotification((int) historyId, "Notification", msg);
      }
    };
  }

  /**
   * Update history for this recording session with the file just recorded
   */
  private Func1<String, String> onFileRecorded(IntentData intentData) {
    return recordedFile -> {
      //todo how to distinct trigger history ?
      History history;
      List<SavedFile> savedFiles;

      // query history for this schedule/trigger
      if (intentData.type() == History.HISTORY_TYPE_SCHEDULE) {
        history =
            rxHistoryService.getHistoryByForeignIdAndType(intentData.foreignId(), intentData.type())
                .toBlocking()
                .first();
      } else {
        history =
            rxHistoryService.getHistoryByForeignIdAndType(intentData.foreignId(), intentData.type())
                .toBlocking()
                .first();
      }

      savedFiles = history.savedFiles();

      SavedFile newFile = SavedFile.create(recordedFile, System.currentTimeMillis(), false);
      savedFiles.add(newFile);

      ContentValues updatedHistory =
          new History.ContentValuesBuilder().id(history.id()).savedFiles(savedFiles).build();

      rxHistoryService.editHistory(updatedHistory).toBlocking().first();

      if (intentData.uploadConfiguration().dropboxUpload()) {
        int positionInList = savedFiles.indexOf(newFile);
        Uri baseUri = Uri.parse(Long.toString(history.id()));
        baseUri = Uri.withAppendedPath(baseUri, Integer.toString(positionInList));
        baseUri = Uri.withAppendedPath(baseUri,
            Boolean.toString(intentData.uploadConfiguration().deleteAfterUpload()));
        Timber.i("Uploading to dropbox... sync call on -> " + Thread.currentThread().getName());
        syncObserver.onChange(true, baseUri);
      }

      return recordedFile;
    };
  }
}
