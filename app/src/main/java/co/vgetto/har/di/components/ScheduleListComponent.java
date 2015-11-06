package co.vgetto.har.di.components;

import co.vgetto.har.di.modules.ScheduleListModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.schedulelist.ScheduleListController;
import co.vgetto.har.ui.schedulelist.ScheduleListLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        ScheduleListModule.class
    }) public interface ScheduleListComponent {

  void inject(ScheduleListLayout scheduleListLayout);

  ScheduleListController controller();
}
