package co.vgetto.har.di.components;

import co.vgetto.har.di.modules.AddOrEditScheduleModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleController;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        AddOrEditScheduleModule.class
    }) public interface AddOrEditScheduleComponent {

  void inject(AddOrEditScheduleLayout addOrEditScheduleLayout);

  AddOrEditScheduleController controller();
}
