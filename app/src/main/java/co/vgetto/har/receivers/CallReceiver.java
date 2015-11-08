package co.vgetto.har.receivers;

import android.content.Context;
import android.support.v7.internal.app.WindowDecorActionBar;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import java.util.Date;
import timber.log.Timber;

/**
 * Created by Kovje on 15.10.2015..
 */
public class CallReceiver extends BaseCallReceiver {

  @Override
  protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    Timber.i("Incoming call started " + number);
  }

  @Override
  protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    Timber.i("Outgoing call started " + number);
  }

  @Override
  protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    Timber.i("Incoming call ended " + number);
    handleAllCalls(TriggerConfiguration.TRIGGER_TYPE_AFTER_INCOMING_CALL, number);
  }

  @Override
  protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    Timber.i("Outgoing call ended " + number);
    handleAllCalls(TriggerConfiguration.TRIGGER_TYPE_AFTER_OUTGOING_CALL, number);
  }

  @Override
  protected void onMissedCall(Context ctx, String number, Date start) {
    Timber.i("Missed call " + number);
    handleAllCalls(TriggerConfiguration.TRIGGER_TYPE_AFTER_MISSED_CALL, number);
  }

  private void handleAllCalls(int type, String phoneNumber) {
    Timber.i(Thread.currentThread().getName());
    Trigger t = rxTriggerService.findTriggerByTypeAndPhoneNumber(type, phoneNumber).toBlocking().first();
    if (t != null) {
      // TODO START RECORDING !
    }
  }
}
