package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.triggerlist.TriggerListController;
import co.vgetto.har.ui.triggerlist.TriggerListPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class TriggerListModule {
  private TriggerListController.ITalkToTriggerListLayout iTalkToTriggerListLayout;

  public TriggerListModule(
      TriggerListController.ITalkToTriggerListLayout iTalkToTriggerListLayout) {
      this.iTalkToTriggerListLayout = iTalkToTriggerListLayout;
  }

  @Provides @ViewScope TriggerListPresenter providesTriggerListPresenter() {
    return new TriggerListPresenter();
  }

  @Provides @ViewScope TriggerListController providesTriggerListController(MainActivityController mainActivityController, RxTriggerService rxTriggerService, TriggerListPresenter presenter) {
   return new TriggerListController(mainActivityController, iTalkToTriggerListLayout, rxTriggerService, presenter);
  }
}
