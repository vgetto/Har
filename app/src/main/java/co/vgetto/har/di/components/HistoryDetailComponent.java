package co.vgetto.har.di.components;

import co.vgetto.har.di.modules.HistoryDetailModule;
import co.vgetto.har.di.modules.HistoryListModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.historydetail.HistoryDetailController;
import co.vgetto.har.ui.historydetail.HistoryDetailLayout;
import co.vgetto.har.ui.historylist.HistoryListController;
import co.vgetto.har.ui.historylist.HistoryListLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        HistoryDetailModule.class
    }) public interface HistoryDetailComponent {

  void inject(HistoryDetailLayout historyDetailLayout);

  HistoryDetailController controller();
}
