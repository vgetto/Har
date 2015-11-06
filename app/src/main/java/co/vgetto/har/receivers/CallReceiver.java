package co.vgetto.har.receivers;

import android.content.Context;
import android.support.v7.internal.app.WindowDecorActionBar;
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
  }

  @Override
  protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    Timber.i("Outgoing call ended " + number);
  }

  @Override
  protected void onMissedCall(Context ctx, String number, Date start) {
    Timber.i("Missed call " + number);
    Timber.i(Thread.currentThread().getName());
  }

}
