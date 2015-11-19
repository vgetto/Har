package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxDbService;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.rxservices.RxUserService;
import co.vgetto.har.syncadapter.SyncObserver;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.schedulelist.ScheduleListController;
import co.vgetto.har.ui.schedulelist.ScheduleListPresenter;
import co.vgetto.har.ui.settings.SettingsController;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class SettingsModule {
  private SettingsController.ITalkToSettingsLayout iTalkToSettingsLayout;

  public SettingsModule(SettingsController.ITalkToSettingsLayout iTalkToSettingsLayout) {
    this.iTalkToSettingsLayout = iTalkToSettingsLayout;
  }

  @Provides @ViewScope SettingsController providesSettingsController(MainActivityController mainActivityController, RxUserService rxUserService, SyncObserver syncObserver) {
   return new SettingsController(mainActivityController, iTalkToSettingsLayout, rxUserService, syncObserver);
  }
}
