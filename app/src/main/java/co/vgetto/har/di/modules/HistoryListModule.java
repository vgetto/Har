package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxHistoryService;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.historylist.HistoryListController;
import co.vgetto.har.ui.historylist.HistoryListPresenter;
import co.vgetto.har.ui.triggerlist.TriggerListController;
import co.vgetto.har.ui.triggerlist.TriggerListPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class HistoryListModule {
  private HistoryListController.ITalkToHistoryListLayout iTalkToHistoryListLayout;

  public HistoryListModule(HistoryListController.ITalkToHistoryListLayout iTalkToHistoryListLayout) {
      this.iTalkToHistoryListLayout = iTalkToHistoryListLayout;
  }

  @Provides @ViewScope HistoryListPresenter providesHistoryListPresenter() {
    return new HistoryListPresenter();
  }

  @Provides @ViewScope HistoryListController providesHistoryListController(MainActivityController mainActivityController, RxHistoryService rxHistoryService, HistoryListPresenter presenter) {
   return new HistoryListController(mainActivityController, iTalkToHistoryListLayout, rxHistoryService, presenter);
  }
}
