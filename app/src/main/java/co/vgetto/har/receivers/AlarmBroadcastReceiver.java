package co.vgetto.har.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import co.vgetto.har.MyApplication;
import co.vgetto.har.audio.RecordAudioService;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.rxservices.RxScheduleService;
import java.util.Date;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Created by Kovje on 9.9.2015..
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
  @Inject RxScheduleService rxScheduleService;

  @Inject Date date;

  PowerManager.WakeLock wl;

  @Override public void onReceive(Context context, Intent intent) {
    // acquire a wake lock so device doesn't go to sleep
    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmBroadcastReceiver");
    wl.acquire();

    Timber.i("Alarm broadcast receiver fired ! " + Long.valueOf(new Date().getTime()));
    MyApplication.get(context).getAppComponent().inject(this);

    long id = intent.getLongExtra("id", 0);

    // if id is valid, start recording !
    if (id != 0) {
      // get schedule from db
      Schedule schedule = rxScheduleService.getScheduleById(id).toBlocking().first();
      RecordAudioService.startRecordingForSchedule(context, schedule);
    }
    wl.release();
  }
}
