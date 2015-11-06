package co.vgetto.har.di.modules;

import co.vgetto.har.rxservices.RxDbService;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.schedulelist.ScheduleListController;
import co.vgetto.har.ui.schedulelist.ScheduleListPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class ScheduleListModule {
  private ScheduleListController.ITalkToScheduleListLayout iTalkToScheduleListLayout;

  public ScheduleListModule(ScheduleListController.ITalkToScheduleListLayout iTalkToScheduleListLayout) {
    this.iTalkToScheduleListLayout = iTalkToScheduleListLayout;
  }

  @Provides @ViewScope ScheduleListPresenter providesScheduleListPresenter() {
    return new ScheduleListPresenter();
  }

  @Provides @ViewScope ScheduleListController providesScheduleListController(MainActivityController mainActivityController, RxDbService rxDbService, RxScheduleService rxScheduleService, ScheduleListPresenter presenter) {
   return new ScheduleListController(mainActivityController, iTalkToScheduleListLayout, rxDbService, rxScheduleService, presenter);
  }
}
