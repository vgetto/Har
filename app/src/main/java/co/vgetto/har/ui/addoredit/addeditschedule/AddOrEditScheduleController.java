package co.vgetto.har.ui.addoredit.addeditschedule;

import co.vgetto.har.Rx;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.addoredit.base.BaseAddEditModel;
import co.vgetto.har.ui.addoredit.model.TriggerConfigurationModel;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.addoredit.base.BaseAddEditController;
import co.vgetto.har.ui.addoredit.base.ITalkToAddEditLayout;
import co.vgetto.har.ui.addoredit.model.RecordingConfigurationModel;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kovje on 26.10.2015..
 */
public class AddOrEditScheduleController implements BaseAddEditController {
  public static final int DATEPICKER_LAYOUT = 0;
  public static final int TIMEPICKER_LAYOUT = 1;
  public static final int RECORDING_CONFIGURATION_LAYOUT = 2;
  public static final int SAVING_AND_NOTIFYING_LAYOUT = 3;
  private AddOrEditScheduleModel model;

  private final MainActivityController mainActivityController;
  private final ITalkToAddEditLayout iTalkToAddEditLayout;
  private final AddOrEditSchedulePresenter presenter;
  private final Calendar calendar;
  private final Date date;
  private final RxScheduleService rxScheduleService;

  public AddOrEditScheduleController(MainActivityController mainActivityController,
      ITalkToAddEditLayout iTalkToAddEditLayout, AddOrEditSchedulePresenter presenter,
      Calendar calendar, Date date, RxScheduleService rxScheduleService) {
    this.mainActivityController = mainActivityController;
    this.iTalkToAddEditLayout = iTalkToAddEditLayout;
    this.presenter = presenter;
    this.calendar = calendar;
    this.date = date;
    this.rxScheduleService = rxScheduleService;
  }

  @Override public void init(BaseModel savedModel) {
    if (savedModel != null) {
      this.model = (AddOrEditScheduleModel) savedModel;
      this.model.setModelRestored(true);
    } else {
      this.model = new AddOrEditScheduleModel(calendar, date);
    }
    iTalkToAddEditLayout.setPage(model.getCurrentPage(), UiConstants.ACTION_INIT);

    if (model.getLayoutType() == BaseAddEditModel.LAYOUT_TYPE_ADD) {
      mainActivityController.setTitle("Create schedule");
    } else {
      mainActivityController.setTitle("Edit schedule");
    }
  }

  @Override public void btnBackClicked() {
    if (model.getCurrentPage() != DATEPICKER_LAYOUT) {
      iTalkToAddEditLayout.setPage(model.previousPage(), UiConstants.ACTION_BACK);
    }
  }

  @Override public void btnNextClicked() {
    if (model.getCurrentPage() == SAVING_AND_NOTIFYING_LAYOUT) {
      // insert new schedule, on success go to previous page, show snackbar
      if (model.getLayoutType() == BaseAddEditModel.LAYOUT_TYPE_ADD) {
        // insert schedule
        rxScheduleService.addSchedule(presenter.getContentValuesFromModel(model))
            .compose(Rx.schedulersIoUi())
            .subscribe(success -> {
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Schedule successfully added..");
              mainActivityController.iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK,
                  null);
            }, error -> {
              // show snackbar error, and go back
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Oops.. error when creating schedule, please try again..");
              mainActivityController.iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK,
                  null);
            });
      } else {
        // edit schedule
        rxScheduleService.editSchedule(presenter.getContentValuesFromModel(model))
            .compose(Rx.schedulersIoUi())
            .subscribe(success -> {
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Schedule successfully edited..");
              mainActivityController.iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK,
                  null);
            }, error -> {
              // show snackbar error, and go back
              mainActivityController.iTalkToMainActivity.showSnackbar(
                  "Oops.. error when editing schedule, please try again..");
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
    if (model.getCurrentPage() == DATEPICKER_LAYOUT) {
      return false;
    } else {
      // if current page isn't the first one (date picker), go back one page
      iTalkToAddEditLayout.setPage(model.previousPage(), UiConstants.ACTION_BACK);
      return true;
    }
  }

  @Override public void dateChanged(List<Integer> newDate) {
    model.setDate(newDate); // set new date to model
    iTalkToAddEditLayout.setButtonsEnabled(false,
        model.dateCheck()); // set appropriate enabled/disable of buttons
  }

  @Override public void timeChanged(List<Integer> newTime) {
    model.setTime(newTime);
    iTalkToAddEditLayout.setButtonsEnabled(true, model.timeCheck());
  }

  @Override
  public void triggerConfigurationChanged(TriggerConfigurationModel triggerConfigurationModel) {
    // not implemented here
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
