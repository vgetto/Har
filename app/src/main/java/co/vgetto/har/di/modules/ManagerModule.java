package co.vgetto.har.di.modules;

import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.Context;
import co.vgetto.har.rxservices.RxDbService;
import co.vgetto.har.rxservices.RxHistoryService;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.di.scopes.ApplicationScope;
import co.vgetto.har.rxservices.RxTriggerService;
import com.squareup.sqlbrite.BriteContentResolver;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class ManagerModule {
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
}
