package co.vgetto.har.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import javax.inject.Inject;
import rx.subjects.PublishSubject;

/**
 * Created by Kovje on 11.8.2015..
 */

/**
 * Android service that is started for every recording session
 */
public class RecordAudioService extends Service {
  @Inject RxAudioRecorder rxAudioRecorder;
  @Inject PublishSubject<Boolean> notificationFromRxRecorder;

  private PowerManager.WakeLock wakeLock;

  public RecordAudioService() {
  }

  public static void startRecordingForTrigger(Context context, Trigger trigger) {
    Bundle b = new Bundle();
    b.putParcelable("trigger", trigger);
    b.putInt("type", History.HISTORY_TYPE_TRIGGER);

    // make intent
    Intent audioIntent = new Intent(context, RecordAudioService.class);
    audioIntent.putExtras(b);

    // start recording !
    context.startService(audioIntent);
  }

  public static void startRecordingForSchedule(Context context, Schedule schedule) {
    Bundle b = new Bundle();
    b.putParcelable("schedule", schedule);
    b.putInt("type", History.HISTORY_TYPE_SCHEDULE);

    // make intent
    Intent audioIntent = new Intent(context, RecordAudioService.class);
    audioIntent.putExtras(b);

    // start recording !
    context.startService(audioIntent);
  }

  /**
   * either a trigger or alarm started the service, time to record!
   * checks for rxAudioRecorder, if it doesn't exist, first creates a PublishSubject
   * (and subscribes to it, so RecordAudioService can get boolean values from RxAudioRecorder),
   * creates the rxAudioRecorder providing context and publish subject for communication
   *
   * if rxAudioRecorder exists, just adds the intent data to queue
   */
  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RecordAudioService");
    wakeLock.acquire();

    // this works because if a startService() is called, and a service of that class
    // is already running, that service will receive an intent
    if (rxAudioRecorder == null) {
      notificationFromRxRecorder = PublishSubject.create();
      // if this observer receives a TRUE boolean value, stop the service with stopSelf()
      notificationFromRxRecorder.filter(aBool -> aBool).subscribe(aBoolean -> {
        stopSelf();
      });
      rxAudioRecorder = new RxAudioRecorder(this, notificationFromRxRecorder);
      rxAudioRecorder.addToQueue(getIntentData(intent));
    } else {
      rxAudioRecorder.addToQueue(getIntentData(intent));
    }

    return START_NOT_STICKY;
  }

  @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onDestroy() {
    rxAudioRecorder = null;
    notificationFromRxRecorder = null;
    wakeLock.release();
    super.onDestroy();
  }

  /**
   * get intent data from intent, store in IntentData object
   */
  public static IntentData getIntentData(Intent intent) {
    Bundle b = intent.getExtras();
    long foreignId;
    int type = b.getInt("type", -1);
    RecordingConfiguration recordingConfiguration;
    UploadConfiguration uploadConfiguration;

    // based on type, get schedule/trigger from bundle, and extracts (foreign) id,
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
    return IntentData.create(foreignId, type, recordingConfiguration, uploadConfiguration);
  }
}
