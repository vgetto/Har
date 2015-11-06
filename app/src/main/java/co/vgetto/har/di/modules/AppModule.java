package co.vgetto.har.di.modules;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import co.vgetto.har.Constants;
import co.vgetto.har.di.scopes.ApplicationScope;
import co.vgetto.har.syncadapter.SyncObserver;
import dagger.Module;
import dagger.Provides;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import timber.log.Timber;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class AppModule {
  private Application application;
  private Calendar calendar;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides @ApplicationScope Application providesApplication() {
    return application;
  }

  @Provides @ApplicationScope SharedPreferences providesSharedPreferences(Application application) {
    return application.getSharedPreferences("HarApp", Context.MODE_PRIVATE);
  }

  @Provides @ApplicationScope Context providesContext() {
    return application;
  }

  @Provides @ApplicationScope ContentResolver providesContentResolver(Context context) {
    return context.getContentResolver();
  }

  @Provides @ApplicationScope Account providesAccountResolver(Context context) {
    Account newAccount = new Account(Constants.ACCOUNT, Constants.ACCOUNT_TYPE);
    // Get an instance of the Android account manager
    AccountManager accountManager =
        (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);

    Account[] existing = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE);


    for (Account a : existing) {
      if (a.equals(newAccount)) {
        Timber.i("Account already exists");
        //ContentResolver.setSyncAutomatically(a, Constants.AUTHORITY, true);
        return a;
      }
    }


    //TODO check possible error here!
    if (accountManager.addAccountExplicitly(newAccount, null, null)) {
      Timber.i("addAccount explicitly");
      //ContentResolver.setSyncAutomatically(newAccount, Constants.AUTHORITY, true);
      return newAccount;
    } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
      Timber.i("error");
      return null;
    }
  }

  @Provides @ApplicationScope AlarmManager providesAlarmManager(Context context) {
    return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }

  @Provides @ApplicationScope SyncObserver providesSyncObserver(Context context,
      ContentResolver resolver) {
    // create sync observer
    SyncObserver observer = new SyncObserver(new Handler(), context);
    // register this observer
    resolver.registerContentObserver(Constants.BASE_URI, true, observer);
    return observer;
  }

  @Provides @ApplicationScope Calendar providesCalendar() {
    calendar = Calendar.getInstance();
    calendar.setTimeZone(TimeZone.getDefault());
    return calendar;
  }

  @Provides @ApplicationScope Date providesDate() {
    return new Date();
  }

}
