package co.vgetto.har.di.components;

import co.vgetto.har.di.modules.RecordingConfigurationModule;
import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.ui.addoredit.recconfig.RecordingConfigurationController;
import co.vgetto.har.ui.addoredit.recconfig.RecordingConfigurationLayout;
import dagger.Subcomponent;

/**
 * Created by Kovje on 23.8.2015..
 */
@ViewScope @Subcomponent(
    modules = {
        RecordingConfigurationModule.class
    }) public interface RecordingConfigurationComponent {

  void inject(RecordingConfigurationLayout recordingConfigurationLayout);

  RecordingConfigurationController controller();
}
