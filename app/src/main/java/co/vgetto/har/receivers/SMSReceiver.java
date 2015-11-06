package co.vgetto.har.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import timber.log.Timber;

/**
 * Created by Kovje on 15.10.2015..
 */
// https://gist.github.com/tom-dignan/2318886
public class SMSReceiver extends BroadcastReceiver {
  /** Tag for identify class in Log */
  private static final String TAG = "SMSReceiver";

  /** Broadcast action for received SMS */
  public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

  /** Broadcast action for sent SMS */
  public static final String ACTION_NEW_OUTGOING_SMS =
      "android.provider.Telephony.NEW_OUTGOING_SMS";

  /** Intent extra for get SMS pdus */
  private static final String EXTRA_PDUS = "pdus";

  /** Processes Intent data into SmsMessage array and calls onSmsReceived */
  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (action != null && (action.equals(
        ACTION_SMS_RECEIVED))) { //|| action.equals(ACTION_NEW_OUTGOING_SMS))) {
      Bundle bundle = intent.getExtras();

      Object messages[] = (Object[]) bundle.get(EXTRA_PDUS);
      SmsMessage smsMessage[] = new SmsMessage[messages.length];

      for (int n = 0; n < messages.length; n++) {
        smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
      }
      String receivedMessage = smsMessage[0].getMessageBody().toString().toUpperCase();
      String originatingAddress = smsMessage[0].getOriginatingAddress();

      Timber.i(originatingAddress + " : " + receivedMessage);
    }
  }
}
