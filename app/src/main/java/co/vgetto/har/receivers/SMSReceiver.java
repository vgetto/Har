package co.vgetto.har.receivers;

import android.content.Context;
import timber.log.Timber;

/**
 * Created by Kovje on 8.11.2015..
 */
public class SMSReceiver extends BaseSMSReceiver {
  @Override protected void onIncomingSMS(Context context, String phoneNumber, String smsText) {
    Timber.i("Sms received from " + phoneNumber + " with text : " + smsText);
    // try to find in db, if exists trigger recording !
  }
}
