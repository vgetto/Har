package co.vgetto.har.ui.addoredit.recconfig;

import co.vgetto.har.Rx;
import co.vgetto.har.db.entities.ConfigurationValue;
import co.vgetto.har.rxservices.RxConfigurationValuesService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import java.util.List;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 19.10.2015..
 */
public class RecordingConfigurationController implements BaseController {
  private BaseModel model;
  private final RxConfigurationValuesService rxConfigurationValuesService;
  private final MainActivityController mainActivityController;
  private final ITalkToRecordingConfigurationLayout iTalkToRecordingConfigurationLayout;

  private Subscription querySubscription;

  public RecordingConfigurationController(MainActivityController mainActivityController,
      ITalkToRecordingConfigurationLayout iTalkToRecordingConfigurationLayout,
      RxConfigurationValuesService rxConfigurationValuesService) {
    this.mainActivityController = mainActivityController;
    this.iTalkToRecordingConfigurationLayout = iTalkToRecordingConfigurationLayout;
    this.rxConfigurationValuesService = rxConfigurationValuesService;
  }

  @Override public void init(BaseModel savedModel) {
    if (model != null) {
      this.model = savedModel;
    } else {
      this.model = new BaseModel();
    }
    setQuerySubscription();
  }

  @Override public BaseModel getModel() {
    return model;
  }

  public void setQuerySubscription() {
    querySubscription = rxConfigurationValuesService.getConfigurationValuesQueryObservable()
        .compose(Rx.schedulersIoUi())
        .subscribe(configurationValues -> iTalkToRecordingConfigurationLayout.setAdapterData(
                configurationValues),
            e -> Timber.i("Error when fetching configuration values from db -> " + e.getMessage()));
  }

  public void clearQuerySubscription() {
    if (querySubscription != null) {
      if (!querySubscription.isUnsubscribed()) {
        querySubscription.unsubscribe();
      }
      querySubscription = null;
    }
  }

  public interface ITalkToRecordingConfigurationLayout {
    void setAdapterData(List<List<ConfigurationValue>> configurationValuesList);
  }
}
