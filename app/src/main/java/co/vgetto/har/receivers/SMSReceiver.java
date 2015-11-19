package co.vgetto.har.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import co.vgetto.har.audio.RecordAudioService;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import timber.log.Timber;

/**
 * Created by Kovje on 8.11.2015..
 */
public class SMSReceiver extends BaseSMSReceiver {
  @Override protected void onIncomingSMS(Context context, String phoneNumber, String smsText) {
    Timber.i("BLOCKING ON THREAD -> " + Thread.currentThread().getName());
    Timber.i("Sms received from " + phoneNumber + " with text : " + smsText);
    // try to find in db, if exists trigger recording !
    Trigger trigger = rxTriggerService.findTriggerByTypeAndPhoneNumber(
        TriggerConfiguration.TRIGGER_TYPE_ON_RECEIVED_SMS, phoneNumber).toBlocking().first();

    // if there is a trigger of "incoming sms" type for this number, and the
    // sms text is equal to received sms, start recording !
    if (trigger != null && trigger.triggerConfiguration().smsText().equals(smsText)) {
      RecordAudioService.startRecordingForTrigger(context, trigger);
    }
  }
}
