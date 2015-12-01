package co.vgetto.har.receivers;

import android.content.Context;
import co.vgetto.har.Rx;
import co.vgetto.har.audio.RecordAudioService;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 8.11.2015..
 */
public class SMSReceiver extends BaseSMSReceiver {
  private Subscription handleSmsSubscription;

  @Override protected void onIncomingSMS(Context context, String phoneNumber, String smsText) {
    Timber.i("BLOCKING ON THREAD -> " + Thread.currentThread().getName());
    Timber.i("Sms received from " + phoneNumber + " with text : " + smsText);
    // try to find in db, if exists trigger recording !
    // if there is a trigger of "incoming sms" type for this number, and the
    // sms text is equal to received sms, start recording !
    handleSmsSubscription = rxTriggerService.findTriggerByTypeAndPhoneNumber(
        TriggerConfiguration.TRIGGER_TYPE_ON_RECEIVED_SMS, phoneNumber)
        .filter(t -> (t != null) && (t.triggerConfiguration().smsText().equals(smsText)))
        .compose(Rx.schedulerIoIo())
        .subscribe(trigger -> RecordAudioService.startRecordingForTrigger(context, trigger),
            e -> Timber.i("Error when handling incoming sms" + e.getMessage()));
  }
}
