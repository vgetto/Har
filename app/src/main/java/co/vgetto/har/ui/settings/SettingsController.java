package co.vgetto.har.ui.settings;

import android.net.Uri;
import android.view.View;
import co.vgetto.har.Rx;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.rxservices.RxDbService;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.rxservices.RxUserService;
import co.vgetto.har.syncadapter.SyncObserver;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.schedulelist.ScheduleListPresenter;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 19.10.2015..
 */
public class SettingsController implements BaseController {
  private BaseModel model;
  private final MainActivityController mainActivityController;
  private final ITalkToSettingsLayout iTalkToSettingsLayout;
  private final RxUserService rxUserService;
  private final SyncObserver syncObserver;

  private Subscription querySubscription;

  public SettingsController(MainActivityController mainActivityController,
      ITalkToSettingsLayout iTalkToSettingsLayout, RxUserService rxUserService,
      SyncObserver syncObserver) {
    this.mainActivityController = mainActivityController;
    this.iTalkToSettingsLayout = iTalkToSettingsLayout;
    this.rxUserService = rxUserService;
    this.syncObserver = syncObserver;
  }

  @Override public void init(BaseModel savedModel) {
    if (model != null) {
      this.model = savedModel;
    } else {
      this.model = new BaseModel();
    }
    mainActivityController.setTitle("Settings");
    setQuerySubscription();
  }

  @Override public BaseModel getModel() {
    return model;
  }

  public void btnLoginClicked() {
    rxUserService.isLoggedToDropbox().compose(Rx.schedulersIoUi()).flatMap(user -> {
      Timber.i("login/logout click - FLATMAP on thread -> " + Thread.currentThread().getName());
      if (user == null) {
        mainActivityController.logIn();
        return Observable.just(0);
      } else {
        return rxUserService.logoutFromDropbox();
      }
    }).subscribe(integer -> {
      if (integer == 0) {
        Timber.i("Logout unsuccessfull");
      } else {
        Timber.i("Logout successfull");
      }
    });
  }

  public void setQuerySubscription() {
    querySubscription = rxUserService.getLoggedToDropboxQueryObservable()
        .compose(Rx.schedulersIoUi())
        .subscribe(user -> {
          if (user != null) {
            iTalkToSettingsLayout.setData("Logged in", "Logout");
          } else {
            iTalkToSettingsLayout.setData("Not logged in", "Log in");
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

  public interface ITalkToSettingsLayout {
    void setData(String tvData, String btnText);
  }
}
