package co.vgetto.har.di.components;

import co.vgetto.har.di.modules.ScheduleListModule;
import co.vgetto.har.di.modules.TriggerListModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.schedulelist.ScheduleListController;
import co.vgetto.har.ui.schedulelist.ScheduleListLayout;
import co.vgetto.har.ui.triggerlist.TriggerListController;
import co.vgetto.har.ui.triggerlist.TriggerListLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        TriggerListModule.class
    }) public interface TriggerListComponent {

  void inject(TriggerListLayout triggerListLayout);

  TriggerListController controller();
}
