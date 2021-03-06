package co.vgetto.har.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.telephony.SmsMessage;
import co.vgetto.har.MyApplication;
import co.vgetto.har.rxservices.RxTriggerService;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Created by Kovje on 15.10.2015..
 */
// https://gist.github.com/tom-dignan/2318886
public abstract class BaseSMSReceiver extends BroadcastReceiver {
  @Inject RxTriggerService rxTriggerService;
  public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
  private static final String EXTRA_PDUS = "pdus";

  private Pair<String, String> getSmsData(Bundle bundle) {
    Object messages[] = (Object[]) bundle.get(EXTRA_PDUS);
    SmsMessage smsMessage[] = new SmsMessage[messages.length];

    for (int n = 0; n < messages.length; n++) {
      smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
    }

    return new Pair<>(smsMessage[0].getOriginatingAddress(), smsMessage[0].getMessageBody().toString());
  }

  @Override public void onReceive(Context context, Intent intent) {
    MyApplication.get(context).getAppComponent().inject(this);

    String action = intent.getAction();
    if (action != null && action.equals(ACTION_SMS_RECEIVED)) {
      Pair<String, String> smsData = getSmsData(intent.getExtras());
      onIncomingSMS(context, smsData.first, smsData.second);
      //Timber.i(action + " " + smsData.first + " : " + smsData.second);
    }
  }

  protected void onIncomingSMS(Context context, String phoneNumber, String smsText) {
  }
}
