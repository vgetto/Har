package co.vgetto.har.di.modules;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import co.vgetto.har.rxservices.RxConfigurationValuesService;
import co.vgetto.har.rxservices.RxDbService;
import co.vgetto.har.rxservices.RxHistoryService;
import co.vgetto.har.rxservices.RxNotificationService;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.di.scopes.ApplicationScope;
import co.vgetto.har.rxservices.RxSharedPreferences;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.rxservices.RxUserService;
import com.dropbox.client2.session.AppKeyPair;
import com.squareup.sqlbrite.BriteContentResolver;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class RxServicesModule {
  @Provides @ApplicationScope RxDbService providesRxDbService(ContentResolver resolver,
      BriteContentResolver briteContentResolver) {
    return new RxDbService(resolver, briteContentResolver);
  }

  @Provides @ApplicationScope RxScheduleService providesRxScheduleService(Context context,
      RxDbService rxDbService, RxHistoryService rxHistoryService, AlarmManager manager) {
    return new RxScheduleService(context, rxDbService, rxHistoryService, manager);
  }

  @Provides @ApplicationScope RxTriggerService providesRxTriggerService(RxDbService rxDbService) {
    return new RxTriggerService(rxDbService);
  }

  @Provides @ApplicationScope RxHistoryService provdesRxHistoryService(Context context, RxDbService rxDbService) {
    return new RxHistoryService(context, rxDbService);
  }

  @Provides @ApplicationScope RxSharedPreferences providesRxSharedPreferences(SharedPreferences sharedPreferences) {
    return new RxSharedPreferences(sharedPreferences);
  }

  @Provides @ApplicationScope RxNotificationService providesRxNotificationService(Context context, NotificationManager notificationManager) {
    return new RxNotificationService(context, notificationManager);
  }

  @Provides @ApplicationScope RxUserService providesRxUserService(RxDbService rxDbService, AppKeyPair appKeyPair) {
    return new RxUserService(rxDbService, appKeyPair);
  }

  @Provides @ApplicationScope RxConfigurationValuesService providerRxConfigurationValuesService(Context context, RxDbService rxDbService) {
    return new RxConfigurationValuesService(context, rxDbService);
  }
}
