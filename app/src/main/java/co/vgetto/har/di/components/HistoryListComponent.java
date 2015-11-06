package co.vgetto.har.di.components;

import co.vgetto.har.db.entities.History;
import co.vgetto.har.di.modules.HistoryListModule;
import co.vgetto.har.di.modules.TriggerListModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.historylist.HistoryListController;
import co.vgetto.har.ui.historylist.HistoryListLayout;
import co.vgetto.har.ui.triggerlist.TriggerListController;
import co.vgetto.har.ui.triggerlist.TriggerListLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        HistoryListModule.class
    }) public interface HistoryListComponent {

  void inject(HistoryListLayout historyListLayout);

  HistoryListController controller();
}
