package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerController;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerPresenter;
import co.vgetto.har.ui.addoredit.base.ITalkToAddEditLayout;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class AddOrEditTriggerModule {
  private ITalkToAddEditLayout iTalkToAddEditLayout;

  public AddOrEditTriggerModule(ITalkToAddEditLayout iTalkToAddEditLayout) {
    this.iTalkToAddEditLayout = iTalkToAddEditLayout;
  }

  @Provides @ViewScope AddOrEditTriggerPresenter providesAddOrEditTriggerPresenter() {
    return new AddOrEditTriggerPresenter();
  }

  @Provides @ViewScope AddOrEditTriggerController providesAddOrEditTriggerController(MainActivityController mainActivityController, AddOrEditTriggerPresenter presenter, RxTriggerService rxTriggerService) {
   return new AddOrEditTriggerController(mainActivityController, iTalkToAddEditLayout, presenter, rxTriggerService);
  }
}
