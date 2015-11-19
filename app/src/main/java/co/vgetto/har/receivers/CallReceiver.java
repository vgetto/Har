package co.vgetto.har.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.app.WindowDecorActionBar;
import co.vgetto.har.audio.RecordAudioService;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import java.util.Date;
import timber.log.Timber;

/**
 * Created by Kovje on 15.10.2015..
 */
public class CallReceiver extends BaseCallReceiver {

  @Override protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    Timber.i("Incoming call started " + number);
  }

  @Override protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    Timber.i("Outgoing call started " + number);
  }

  @Override protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    Timber.i("Incoming call ended " + number);
    handleAllCalls(ctx, TriggerConfiguration.TRIGGER_TYPE_AFTER_INCOMING_CALL, number);
  }

  @Override protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    Timber.i("Outgoing call ended " + number);
    handleAllCalls(ctx, TriggerConfiguration.TRIGGER_TYPE_AFTER_OUTGOING_CALL, number);
  }

  @Override protected void onMissedCall(Context ctx, String number, Date start) {
    Timber.i("Missed call " + number);
    handleAllCalls(ctx, TriggerConfiguration.TRIGGER_TYPE_AFTER_MISSED_CALL, number);
  }

  private void handleAllCalls(Context context, int type, String phoneNumber) {
    //todo blocks on main thread, don't block the main thread!
    Timber.i("BLOCKING ON THREAD -> " + Thread.currentThread().getName());
    Trigger trigger =
        rxTriggerService.findTriggerByTypeAndPhoneNumber(type, phoneNumber).toBlocking().first();

    // if trigger for this phone number and call type exist, start recording
    if (trigger != null) {
      RecordAudioService.startRecordingForTrigger(context, trigger);
    }
  }
}
