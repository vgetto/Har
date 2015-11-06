package co.vgetto.har.syncadapter;

import android.accounts.Account;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import co.vgetto.har.MyApplication;
import javax.inject.Inject;

/**
 * Created by Kovje on 8.9.2015..
 */
public class SyncObserver extends ContentObserver {
  @Inject Account account;

  public SyncObserver(Handler handler, Context context) {
    super(handler);
    MyApplication.get(context).getAppComponent().inject(this);
    //ContentResolver.setSyncAutomatically(account, Constants.AUTHORITY, true);
  }

  @Override public boolean deliverSelfNotifications() {
    return true;
  }

  @Override public void onChange(boolean selfChange) {
            /*
             * Invoke the method signature available as of
             * Android platform version 4.1, with a null URI.
             */
    onChange(selfChange, null);
  }

  /*
   * Define a method that's called when data in the
   * observed content provider changes.
   */
  @Override public void onChange(boolean selfChange, Uri changeUri) {
    //Bundle b = new Bundle();
    //b.putString("uri", changeUri.toString());
    //ContentResolver.requestSync(account, Constants.AUTHORITY, b);
    // onChange is a do nothing bitch for now
  }
}
