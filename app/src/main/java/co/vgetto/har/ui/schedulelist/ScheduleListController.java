package co.vgetto.har.ui.schedulelist;

import co.vgetto.har.Rx;
import co.vgetto.har.rxservices.RxDbService;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import java.util.List;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 19.10.2015..
 */
public class ScheduleListController implements BaseController {
  private BaseModel model;
  private final RxDbService rxDbService;
  private final RxScheduleService rxScheduleService;
  private final ScheduleListPresenter scheduleListPresenter;
  private final MainActivityController mainActivityController;
  private final ITalkToScheduleListLayout iTalkToScheduleListLayout;

  private Subscription querySubscription;

  public ScheduleListController(MainActivityController mainActivityController,
      ITalkToScheduleListLayout iTalkToScheduleListLayout, RxDbService rxDbService, RxScheduleService rxScheduleService,
      ScheduleListPresenter presenter) {
    this.mainActivityController = mainActivityController;
    this.iTalkToScheduleListLayout = iTalkToScheduleListLayout;
    this.rxDbService = rxDbService;
    this.rxScheduleService = rxScheduleService;
    this.scheduleListPresenter = presenter;
  }

  @Override public void init(BaseModel savedModel) {
    if (model != null) {
      this.model = savedModel;
    } else {
      this.model = new BaseModel();
    }
    mainActivityController.setTitle("Schedules");
    setQuerySubscription();
  }

  @Override public BaseModel getModel() {
    return model;
  }

  public void addSchedule() {
    mainActivityController.iTalkToMainActivity.showLayout(UiConstants.ADD_OR_EDIT_SCHEDULE,
        UiConstants.ACTION_ADD_ON_TOP, null);
  }

  public void editSchedule(Schedule schedule) {
    mainActivityController.iTalkToMainActivity.showLayout(UiConstants.ADD_OR_EDIT_SCHEDULE,
        UiConstants.ACTION_ADD_ON_TOP, AddOrEditScheduleModel.createFromSchedule(schedule));
  }

  public void deleteSchedule(Schedule schedule) {
    rxScheduleService.deleteSchedule(schedule)
        .compose(Rx.schedulersIoUi())
        .subscribe(rowsAffected -> {
          String msg;
          if (rowsAffected != 0) {
            msg = "Schedule succesfully deleted";
          } else {
            msg = "Error when deleting schedule";
          }
          mainActivityController.iTalkToMainActivity.showSnackbar(msg);
        }, error -> mainActivityController.iTalkToMainActivity.showSnackbar(
            "Error while deleting schedule"));
  }

  public void setQuerySubscription() {
    querySubscription =
        rxScheduleService.getScheduleQueryObservable().compose(Rx.schedulersIoUi()).subscribe(schedules -> {
          iTalkToScheduleListLayout.setAdapterData(schedules);
          for (Schedule s : schedules) {
            Timber.i(Long.toString(s.id()) + " file prefix ->" + s.scheduleConfiguration()
                .recordingConfiguration()
                .filePrefix());
          }
        }, e -> Timber.i("Error when opening schedule query " + e.getMessage()));
  }

  public void clearQuerySubscription() {
    if (querySubscription != null) {
      if (!querySubscription.isUnsubscribed()) {
        querySubscription.unsubscribe();
      }
      querySubscription = null;
    }
  }

  public interface ITalkToScheduleListLayout {
    void setAdapterData(List<Schedule> scheduleList);

    void showFab(boolean show);
  }
}
