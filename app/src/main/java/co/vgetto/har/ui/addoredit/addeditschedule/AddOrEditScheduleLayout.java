package co.vgetto.har.ui.addoredit.addeditschedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.MyApplication;
import co.vgetto.har.R;
import co.vgetto.har.Rx;
import co.vgetto.har.di.modules.AddOrEditScheduleModule;
import co.vgetto.har.ui.animation.AnimateEntrance;
import co.vgetto.har.ui.animation.AnimateExit;
import co.vgetto.har.ui.animation.BaseAnimationObservable;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.addoredit.base.BaseAddEditLayout;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import co.vgetto.har.ui.addoredit.rx.PageSubscriptions;
import com.jakewharton.rxbinding.view.RxView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Kovje on 26.10.2015..
 */
public class AddOrEditScheduleLayout extends LinearLayout implements BaseAddEditLayout {
  @Inject AddOrEditScheduleController controller;

  @Bind(R.id.btnBack) Button btnBack;

  @Bind(R.id.btnNext) Button btnNext;

  @Bind(R.id.configurationContainer) LinearLayout configurationContainer;

  private View currentView;

  private LayoutInflater inflater;

  private Observable<Integer> entranceAnimationObservable, exitAnimationObservable;

  private Subscription layoutChangesSubscription, btnBackSubscription, btnNextSubscription,
      currentLayoutSubscription;

  private final List<View> pageList = new ArrayList<>();

  private BaseModel model;


  public AddOrEditScheduleLayout(Context context, BaseModel model) {
    this(context, null, model);
  }

  public AddOrEditScheduleLayout(Context context, AttributeSet attrs,
      BaseModel model) {
    this(context, attrs, 0, model);
  }

  public AddOrEditScheduleLayout(Context context, AttributeSet attrs, int defStyleAttr,
      BaseModel model) {
    super(context, attrs, defStyleAttr);
    this.model = model;
    // inflate base scheduleConfiguration layout, empty linear layout and 2 buttons under it
    LayoutInflater.from(context).inflate(R.layout.configuration_layout, this, true);

    ButterKnife.bind(this);

    MyApplication.get(getContext())
        .getMainActivityComponent()
        .plus(new AddOrEditScheduleModule(this))
        .inject(this);

    inflater = LayoutInflater.from(context);

    pageList.add(inflater.inflate(R.layout.date_picker_layout, null));
    pageList.add(inflater.inflate(R.layout.time_picker_layout, null));
    pageList.add(inflater.inflate(R.layout.recording_configuration_layout, null));
    pageList.add(inflater.inflate(R.layout.upload_configuration_layout, null));
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    controller.init(model);
    setButtonSubscriptions();
  }

  @Override protected void onDetachedFromWindow() {
    if (layoutChangesSubscription != null) {
      layoutChangesSubscription.unsubscribe();
      layoutChangesSubscription = null;
    }

    if (currentLayoutSubscription != null) {
      if (!currentLayoutSubscription.isUnsubscribed()) {
        currentLayoutSubscription.unsubscribe();
      }
      currentLayoutSubscription = null;
    }

    if (btnBackSubscription != null) {
      if (!btnBackSubscription.isUnsubscribed()) {
        btnBackSubscription.unsubscribe();
      }
      btnBackSubscription = null;
    }

    if (btnNextSubscription != null) {
      if (!btnNextSubscription.isUnsubscribed()) {
        btnNextSubscription.unsubscribe();
      }
      btnNextSubscription = null;
    }
    super.onDetachedFromWindow();
  }

  @Override public void setButtonSubscriptions() {
    btnBackSubscription = RxView.clickEvents(btnBack)
        .compose(Rx.schedulersUiUi())
        .subscribe(viewClickEvent -> {
          controller.btnBackClicked();
          setButtonsEnabled(false, false);
        });
    btnNextSubscription = RxView.clickEvents(btnNext)
        .compose(Rx.schedulersUiUi())
        .subscribe(viewClickEvent -> {
          controller.btnNextClicked();
          setButtonsEnabled(false, false);
        });
  }

  @Override public void setCurrentPageSubscription(int currentPage) {
    if (currentLayoutSubscription != null) {
      currentLayoutSubscription.unsubscribe();
      currentLayoutSubscription = null;
    }

    AddOrEditScheduleModel model = (AddOrEditScheduleModel) controller.getModel();

    // make like observable = Pagesubscriptions.getObservableById(currentPage, currentView, model)
    // then a switch() statement with subscribe() and call to controller based on currentPage
    // move compose to Pagesubscriptions

    switch (currentPage) {
      case AddOrEditScheduleController.DATEPICKER_LAYOUT:
        setButtonsEnabled(false, false);
        currentLayoutSubscription =
            PageSubscriptions.setDatePickerObservable(currentView, model.getCurrentDateAsList())
                .subscribe(integers -> controller.dateChanged(integers));
        break;
      case AddOrEditScheduleController.TIMEPICKER_LAYOUT:
        setButtonsEnabled(true, false);
        currentLayoutSubscription =
            PageSubscriptions.setTimePickerObservable(currentView, model.getCurrentDateString(),
                model.getCurrentTimeAsList())
                .subscribe(integers -> controller.timeChanged(integers));
        break;
      case AddOrEditScheduleController.RECORDING_CONFIGURATION_LAYOUT:
        setButtonsEnabled(true, false);
        currentLayoutSubscription =
            PageSubscriptions.setRecordingConfigurationObservable(currentView, model)
                .subscribe(recordingConfigurationModel -> controller.recordingConfigurationChanged(
                    recordingConfigurationModel));
        break;
      case AddOrEditScheduleController.SAVING_AND_NOTIFYING_LAYOUT:
        setButtonsEnabled(true, true);
        currentLayoutSubscription =
            PageSubscriptions.setUploadConfigurationObservable(currentView, model)
                .subscribe(uploadConfigurationModel -> controller.uploadConfigurationChanged(
                    uploadConfigurationModel));
        break;
    }
  }

  @Override public BaseController getController() {
    return controller;
  }

  @Override public BaseModel getSavedModelFromLayout() {
    return controller.getModel();
  }

  @Override public boolean setButtonsEnabled(boolean back, boolean next) {
    RxView.enabled(btnBack).call(back);
    RxView.enabled(btnNext).call(next);
    return back & next;
  }

  @Override public void setPage(int currentPage, int action) {
    if (layoutChangesSubscription != null) {
      layoutChangesSubscription.unsubscribe();
      layoutChangesSubscription = null;
    }

    if (action == UiConstants.ACTION_INIT) {
      configurationContainer.addView(pageList.get(currentPage));
      currentView = configurationContainer.getChildAt(0);
      setCurrentPageSubscription(currentPage);
    } else {
      switch (action) {
        case UiConstants.ACTION_NEXT:
          exitAnimationObservable = Observable.create(
              new AnimateExit(configurationContainer, currentView,
                  BaseAnimationObservable.MOVE_TO_LEFT));

          entranceAnimationObservable = Observable.create(
              new AnimateEntrance(configurationContainer, pageList.get(currentPage),
                  BaseAnimationObservable.MOVE_FROM_RIGHT));
          break;

        case UiConstants.ACTION_BACK:
          exitAnimationObservable = Observable.create(
              new AnimateExit(configurationContainer, currentView,
                  BaseAnimationObservable.MOVE_TO_RIGHT));

          entranceAnimationObservable = Observable.create(
              new AnimateEntrance(configurationContainer, pageList.get(currentPage),
                  BaseAnimationObservable.MOVE_FROM_LEFT));
          break;
      }

      layoutChangesSubscription =
          Observable.zip(exitAnimationObservable, entranceAnimationObservable,
              (Integer exitAnimation, Integer entranceAnimation) -> entranceAnimation)
              .compose(Rx.schedulersUiUi())
              .subscribe(integer -> {
                // when entrance animation finishes, get current view and call presenter to setSubscription
                if (integer == BaseAnimationObservable.ANIMATION_END) {
                  currentView = configurationContainer.getChildAt(0);
                  setCurrentPageSubscription(currentPage);
                }
              });
    }
  }
}
