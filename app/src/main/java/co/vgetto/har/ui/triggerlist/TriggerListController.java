package co.vgetto.har.ui.triggerlist;

import co.vgetto.har.Rx;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.addoredit.model.AddOrEditTriggerModel;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import java.util.List;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 19.10.2015..
 */
public class TriggerListController implements BaseController {
  private BaseModel model;
  private final RxTriggerService rxTriggerService;
  private final TriggerListPresenter triggerListPresenter;
  private final MainActivityController mainActivityController;
  private final ITalkToTriggerListLayout iTalkToTriggerListLayout;

  private Subscription querySubscription;

  public TriggerListController(MainActivityController mainActivityController,
      ITalkToTriggerListLayout iTalkToTriggerListLayout, RxTriggerService rxTriggerService,
      TriggerListPresenter presenter) {
    this.mainActivityController = mainActivityController;
    this.iTalkToTriggerListLayout = iTalkToTriggerListLayout;
    this.rxTriggerService = rxTriggerService;
    this.triggerListPresenter = presenter;
  }

  @Override public void init(BaseModel savedModel) {
    if (model != null) {
      this.model = savedModel;
    } else {
      this.model = new BaseModel();
    }
    mainActivityController.setTitle("Triggers");
    setQuerySubscription();
  }

  @Override public BaseModel getModel() {
    return model;
  }

  public void addTrigger() {
    mainActivityController.iTalkToMainActivity.showLayout(UiConstants.ADD_OR_EDIT_TRIGGER, UiConstants.ACTION_ADD_ON_TOP, null);
  }

  public void editTrigger(Trigger trigger) {
    mainActivityController.iTalkToMainActivity.showLayout(UiConstants.ADD_OR_EDIT_TRIGGER,
        UiConstants.ACTION_ADD_ON_TOP, AddOrEditTriggerModel.createFromTrigger(trigger));
  }

  public void deleteTrigger(Trigger trigger) {

    rxTriggerService.deleteTrigger(trigger.id())
        .compose(Rx.schedulersIoUi())
        .subscribe(rowsAffected -> {
          String msg;
          if (rowsAffected != 0) {
            msg = "Trigger succesfully deleted";
          } else {
            msg = "Error when deleting trigger";
          }
          mainActivityController.iTalkToMainActivity.showSnackbar(msg);
        }, error -> mainActivityController.iTalkToMainActivity.showSnackbar(
            "Error while deleting trigger"));

  }

  public void setQuerySubscription() {

    querySubscription =
        rxTriggerService.getTriggerQueryObservable().compose(Rx.schedulersIoUi()).subscribe(triggers -> {
          iTalkToTriggerListLayout.setAdapterData(triggers);
          for (Trigger t : triggers) {
            Timber.i(Long.toString(t.id()) + " file prefix ->" + t.triggerConfiguration()
                .recordingConfiguration()
                .filePrefix());
          }
        });

  }

  public void clearQuerySubscription() {
    if (querySubscription != null) {
      if (!querySubscription.isUnsubscribed()) {
        querySubscription.unsubscribe();
      }
      querySubscription = null;
    }
  }

  public interface ITalkToTriggerListLayout {
    void setAdapterData(List<Trigger> triggerList);

    void showFab(boolean show);
  }
}
