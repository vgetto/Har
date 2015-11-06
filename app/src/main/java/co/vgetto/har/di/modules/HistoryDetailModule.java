package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxHistoryService;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.historydetail.HistoryDetailController;
import co.vgetto.har.ui.historydetail.HistoryDetailPresenter;
import co.vgetto.har.ui.historylist.HistoryListPresenter;
import co.vgetto.har.ui.triggerlist.TriggerListController;
import co.vgetto.har.ui.triggerlist.TriggerListPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class HistoryDetailModule {
  private HistoryDetailController.ITalkToHistoryDetailLayout iTalkToHistoryDetailLayout;

  public HistoryDetailModule(
      HistoryDetailController.ITalkToHistoryDetailLayout iTalkToHistoryDetailLayout) {
    this.iTalkToHistoryDetailLayout = iTalkToHistoryDetailLayout;
  }

  @Provides @ViewScope HistoryDetailPresenter providesHistoryDetailPresenter() {
    return new HistoryDetailPresenter();
  }

  @Provides @ViewScope HistoryDetailController providesHistoryDetailController(
      MainActivityController mainActivityController, RxHistoryService rxHistoryService,
      HistoryDetailPresenter presenter) {
    return new HistoryDetailController(mainActivityController, iTalkToHistoryDetailLayout, rxHistoryService, presenter);
  }
}
