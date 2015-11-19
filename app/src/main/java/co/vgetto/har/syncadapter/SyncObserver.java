package co.vgetto.har.syncadapter;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import co.vgetto.har.Constants;
import co.vgetto.har.MyApplication;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

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
    List<String> pathSegments = changeUri.getPathSegments();
    if (pathSegments.size() == 3) {
      Bundle b = new Bundle();
      b.putLong("historyId", Long.valueOf(pathSegments.get(0)));
      b.putInt("positionInList", Integer.valueOf(pathSegments.get(1)));
      b.putBoolean("deleteAfterUpload", Boolean.valueOf(pathSegments.get(2)));
      ContentResolver.requestSync(account, Constants.AUTHORITY, b);
    }
  }
}
