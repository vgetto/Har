package co.vgetto.har.di.components;

import co.vgetto.har.di.modules.ScheduleListModule;
import co.vgetto.har.di.modules.SettingsModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.schedulelist.ScheduleListController;
import co.vgetto.har.ui.schedulelist.ScheduleListLayout;
import co.vgetto.har.ui.settings.SettingsController;
import co.vgetto.har.ui.settings.SettingsLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        SettingsModule.class
    }) public interface SettingsComponent {

  void inject(SettingsLayout settingsLayout);

  SettingsController controller();
}
