package co.vgetto.har.receivers;

import android.content.Context;
import co.vgetto.har.Rx;
import co.vgetto.har.audio.RecordAudioService;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import java.util.Date;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 15.10.2015..
 */
public class CallReceiver extends BaseCallReceiver {
  private Subscription handleCallSubscription;

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
    // if trigger for this phone number and call type exist, start recording
    handleCallSubscription = rxTriggerService.findTriggerByTypeAndPhoneNumber(type, phoneNumber)
        .compose(Rx.schedulerIoIo())
        .filter(trigger -> trigger != null)
        .subscribe(triggerNotNull -> RecordAudioService.startRecordingForTrigger(context, triggerNotNull),
            error -> Timber.i("Error when handling call -> " + error.getMessage()));


  }
}
