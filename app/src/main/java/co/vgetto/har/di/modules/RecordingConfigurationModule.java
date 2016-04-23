package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxConfigurationValuesService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.addoredit.recconfig.RecordingConfigurationController;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class RecordingConfigurationModule {
  private RecordingConfigurationController.ITalkToRecordingConfigurationLayout iTalkToRecordingConfigurationLayout;

  public RecordingConfigurationModule(
      RecordingConfigurationController.ITalkToRecordingConfigurationLayout iTalkToRecordingConfigurationLayout) {
    this.iTalkToRecordingConfigurationLayout = iTalkToRecordingConfigurationLayout;
  }

  @Provides @ViewScope RecordingConfigurationController providerRecordingConfigurationController(MainActivityController mainActivityController, RxConfigurationValuesService rxConfigurationValuesService) {
   return new RecordingConfigurationController(mainActivityController, iTalkToRecordingConfigurationLayout, rxConfigurationValuesService);
  }
}
