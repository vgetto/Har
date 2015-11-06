package co.vgetto.har.ui.historylist;

import android.util.Log;
import co.vgetto.har.Rx;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.rxservices.RxHistoryService;
import co.vgetto.har.rxservices.RxTriggerService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.addoredit.model.AddOrEditTriggerModel;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.historydetail.HistoryDetailModel;
import co.vgetto.har.ui.triggerlist.TriggerListPresenter;
import java.util.List;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 19.10.2015..
 */
public class HistoryListController implements BaseController {
  private BaseModel model;
  private final RxHistoryService rxHistoryService;
  private final HistoryListPresenter historyListPresenter;
  private final MainActivityController mainActivityController;
  private final ITalkToHistoryListLayout iTalkToHistoryListLayout;

  private Subscription querySubscription;

  public HistoryListController(MainActivityController mainActivityController,
      ITalkToHistoryListLayout iTalkToHistoryListLayout, RxHistoryService rxHistoryService,
      HistoryListPresenter presenter) {
    this.mainActivityController = mainActivityController;
    this.iTalkToHistoryListLayout = iTalkToHistoryListLayout;
    this.rxHistoryService = rxHistoryService;
    this.historyListPresenter = presenter;
  }

  @Override public void init(BaseModel savedModel) {
    if (savedModel != null) {
      this.model = savedModel;
    } else {
      this.model = new BaseModel();
    }
    mainActivityController.setTitle("History");
    setQuerySubscription();
  }

  @Override public BaseModel getModel() {
    return model;
  }

  public void openHistoryDetails(History h) {
    HistoryDetailModel historyDetailModel = new HistoryDetailModel();
    historyDetailModel.setHistoryId(h.id());
    mainActivityController.iTalkToMainActivity.showLayout(UiConstants.HISTORY_DETAILS,
        UiConstants.ACTION_ADD_ON_TOP, historyDetailModel);
  }

  public void setQuerySubscription() {
    querySubscription =
        rxHistoryService.getHistoryQueryObservable().compose(Rx.schedulersIoUi()).subscribe(history -> {
          iTalkToHistoryListLayout.setAdapterData(history);
          for (History h : history) {
            Timber.i("History item -> id = " + Long.toString(h.id()));
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

  public interface ITalkToHistoryListLayout {
    void setAdapterData(List<History> history);
  }
}
