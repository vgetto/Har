package co.vgetto.har.ui.historydetail;

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
import co.vgetto.har.ui.historylist.HistoryListController;
import co.vgetto.har.ui.historylist.HistoryListPresenter;
import co.vgetto.har.ui.triggerlist.TriggerListPresenter;
import java.util.List;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 19.10.2015..
 */
public class HistoryDetailController implements BaseController {
  private HistoryDetailModel model;
  private final MainActivityController mainActivityController;
  private final ITalkToHistoryDetailLayout iTalkToHistoryDetailLayout;
  private final RxHistoryService rxHistoryService;
  private final HistoryDetailPresenter historyDetailPresenter;

  private Subscription querySubscription;

  public HistoryDetailController(MainActivityController mainActivityController,
      ITalkToHistoryDetailLayout iTalkToHistoryDetailLayout, RxHistoryService rxHistoryService,
      HistoryDetailPresenter historyListPresenter) {
    this.mainActivityController = mainActivityController;
    this.iTalkToHistoryDetailLayout = iTalkToHistoryDetailLayout;
    this.rxHistoryService = rxHistoryService;
    this.historyDetailPresenter = historyListPresenter;
  }

  @Override public void init(BaseModel savedModel) {
    if (savedModel != null) {
      this.model = (HistoryDetailModel)savedModel;
    } else {
      this.model = new HistoryDetailModel();
    }
    mainActivityController.setTitle("History detail");
    setQuerySubscription();
  }

  @Override public BaseModel getModel() {
    return model;
  }

  public void click(History h) {

  }

  private void setQuerySubscription() {
    querySubscription = rxHistoryService.getHistoryQueryObservable()
        .map(histories -> {
          for (History h : histories) {
            if (h.id() == model.getHistoryId()) {
              return h;
            }
          }
          return null;
        })
        .compose(Rx.schedulersIoUi())
        .subscribe(historyItem -> {
          // set data
          if (historyItem != null) {
            iTalkToHistoryDetailLayout.setData(historyItem);
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

  public interface ITalkToHistoryDetailLayout {
    void setData(History h);
  }
}
