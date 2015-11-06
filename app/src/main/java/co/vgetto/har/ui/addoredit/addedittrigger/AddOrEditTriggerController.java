package co.vgetto.har.ui.addoredit.addedittrigger;

import co.vgetto.har.Rx;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.addoredit.base.BaseAddEditModel;
import co.vgetto.har.ui.addoredit.model.AddOrEditTriggerModel;
import co.vgetto.har.ui.addoredit.model.RecordingConfigurationModel;
import co.vgetto.har.ui.addoredit.model.TriggerConfigurationModel;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import co.vgetto.har.ui.addoredit.base.BaseAddEditController;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.addoredit.base.ITalkToAddEditLayout;
import java.util.List;

/**
 * Created by Kovje on 26.10.2015..
 */
public class AddOrEditTriggerController implements BaseAddEditController {
  public static final int TRIGGER_CONFIGURATION = 0;
  public static final int RECORDING_CONFIGURATION_LAYOUT = 1;
  public static final int SAVING_AND_NOTIFYING_LAYOUT = 2;

  private AddOrEditTriggerModel model;

  private final MainActivityController mainActivityController;
  private final ITalkToAddEditLayout iTalkToAddEditLayout;
  private final AddOrEditTriggerPresenter presenter;
  private final RxTriggerService rxTriggerService;

  public AddOrEditTriggerController(MainActivityController mainActivityController,
      ITalkToAddEditLayout iTalkToAddEditLayout, AddOrEditTriggerPresenter presenter, RxTriggerService rxTriggerService) {
    this.mainActivityController = mainActivityController;
    this.iTalkToAddEditLayout = iTalkToAddEditLayout;
    this.presenter = presenter;
    this.rxTriggerService = rxTriggerService;
  }

  @Override public void init(BaseModel savedModel) {
    if (savedModel != null) {
      this.model = (AddOrEditTriggerModel) savedModel;
      this.model.setModelRestored(true);
    } else {
      this.model = new AddOrEditTriggerModel();
    }
    iTalkToAddEditLayout.setPage(model.getCurrentPage(), UiConstants.ACTION_INIT);

    if (model.getLayoutType() == BaseAddEditModel.LAYOUT_TYPE_ADD) {
      mainActivityController.setTitle("Create trigger");
    } else {
      mainActivityController.setTitle("Edit trigger");
    }
  }

  @Override public void btnBackClicked() {
    if (model.getCurrentPage() != TRIGGER_CONFIGURATION) {
      iTalkToAddEditLayout.setPage(model.previousPage(), UiConstants.ACTION_BACK);
    }
  }

  @Override public void btnNextClicked() {
    if (model.getCurrentPage() == SAVING_AND_NOTIFYING_LAYOUT) {
      // insert new schedule, on success go to previous page, show snackbar
      if (model.getLayoutType() == BaseAddEditModel.LAYOUT_TYPE_ADD) {
        // insert trigger
        rxTriggerService.insertTrigger(presenter.getContentValuesFromModel(model))
            .compose(Rx.schedulersIoUi())
            .subscribe(uri -> {
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Trigger successfully added..");
              mainActivityController.iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK,
                  null);
            }, error -> {
              // show snackbar error, and go back
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Oops.. error when creating trigger, please try again..");
              mainActivityController.iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK,
                  null);
            });

      } else {
        // edit trigger
        rxTriggerService.editTrigger(presenter.getContentValuesFromModel(model))
            .compose(Rx.schedulersIoUi())
            .subscribe(uri -> {
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Trigger successfully edited..");
              mainActivityController.iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK,
                  null);
            }, error -> {
              // show snackbar error, and go back
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Oops.. error when editing trigger, please try again..");
              mainActivityController.iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK,
                  null);
            });

      }
    } else {
      iTalkToAddEditLayout.setPage(model.nextPage(), UiConstants.ACTION_NEXT);
    }
  }

  @Override public boolean onBackPressed() {
    // if first page (date picker) , return to previous layout
    if (model.getCurrentPage() == TRIGGER_CONFIGURATION) {
      return false;
    } else {
      // if current page isn't the first one (date picker), go back one page
      iTalkToAddEditLayout.setPage(model.previousPage(), UiConstants.ACTION_BACK);
      return true;
    }
  }

  @Override public void dateChanged(List<Integer> newDate) {
  }

  @Override public void timeChanged(List<Integer> newTime) {
  }

  @Override public void triggerConfigurationChanged(TriggerConfigurationModel triggerConfigurationModel) {
    model.setTriggerConfigurationModel(triggerConfigurationModel);
    iTalkToAddEditLayout.setButtonsEnabled(false, true); // todo add check for type/phonenumber
  }

  @Override public void recordingConfigurationChanged(
      RecordingConfigurationModel recordingConfigurationModel) {
    model.setRecordingConfigurationModel(recordingConfigurationModel);
    iTalkToAddEditLayout.setButtonsEnabled(true, model.recordingConfigurationCheck());
  }

  @Override
  public void uploadConfigurationChanged(UploadConfigurationModel uploadConfigurationModel) {
    model.setUploadConfigurationModel(uploadConfigurationModel);
  }

  @Override public BaseModel getModel() {
    return model;
  }
}
