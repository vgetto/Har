package co.vgetto.har.di.components;

import co.vgetto.har.di.modules.AddOrEditScheduleModule;
import co.vgetto.har.di.modules.AddOrEditTriggerModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleController;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleLayout;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerController;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        AddOrEditTriggerModule.class
    }) public interface AddOrEditTriggerComponent {

  void inject(AddOrEditTriggerLayout addOrEditTriggerLayout);

  AddOrEditTriggerController controller();
}
