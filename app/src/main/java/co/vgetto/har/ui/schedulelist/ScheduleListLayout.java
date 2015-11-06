package co.vgetto.har.ui.schedulelist;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.MyApplication;
import co.vgetto.har.R;
import co.vgetto.har.Rx;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.di.modules.ScheduleListModule;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseLayout;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.rxanimation.RxViewAnimation;
import com.jakewharton.rxbinding.view.RxView;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 25.9.2015..
 */
public class ScheduleListLayout extends FrameLayout implements
    ScheduleListController.ITalkToScheduleListLayout, ScheduleAdapter.ScheduleListClicks,
    BaseLayout {
  @Inject Context context;

  @Inject ScheduleListController controller;

  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  @Bind(R.id.fabBtn) FloatingActionButton fab;

  public LinearLayoutManager linearLayoutManager;

  private Subscription fabSubscription;

  private LayoutInflater inflater;

  private boolean fabShown;

  public ScheduleListLayout(Context context) {
    this(context, null);
  }

  public ScheduleListLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ScheduleListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    MyApplication.get(getContext())
        .getMainActivityComponent()
        .plus(new ScheduleListModule(this))
        .inject(this);

    inflater = LayoutInflater.from(context);
    inflateLayout();
    initRecyclerView();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    controller.init(null);
    setFabSubscription();
  }

  @Override protected void onDetachedFromWindow() {
    controller.clearQuerySubscription();
    clearFabSubscription();
    super.onDetachedFromWindow();
  }

  public void setFabSubscription() {
    fabSubscription = RxView.clickEvents(fab)
        .compose(Rx.schedulersUiUi())
        .subscribe(viewClickEvent -> controller.addSchedule());
  }

  public void clearFabSubscription() {
    if (fabSubscription != null) {
      if (!fabSubscription.isUnsubscribed()) {
        fabSubscription.unsubscribe();
      }
      fabSubscription = null;
    }
  }

  public void inflateLayout() {
    inflater.inflate(R.layout.recyclerview_fab_layout, this, true);
    ButterKnife.bind(this);
  }

  public Pair<Float, Float> getFabPosition() {
    return new Pair<Float, Float>(fab.getX(), fab.getY());
  }

  public void initRecyclerView() {
    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    recyclerView.setHasFixedSize(true);

    // use a linear layout manager
    linearLayoutManager = new LinearLayoutManager(context);
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(new ScheduleAdapter(this, null));

    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        Timber.i(Integer.toBinaryString(linearLayoutManager.findFirstVisibleItemPosition()));
        int firstVisible = linearLayoutManager.findFirstVisibleItemPosition();
        if ((firstVisible == 0) && !fabShown) {
          showFab(true);
        } else if ((firstVisible != 0) && fabShown) {
          showFab(false);
        }
      }
    });
  }

  // schedule list clicks interface method implementations

  @Override public void editClicked(Schedule s) {
    controller.editSchedule(s);
  }

  @Override public void deleteClicked(Schedule s) {
    controller.deleteSchedule(s);
  }

  // ITalkToScheduleListLayout interface implementation

  @Override public void setAdapterData(List<Schedule> scheduleList) {
    recyclerView.setAdapter(new ScheduleAdapter(this, scheduleList));
  }

  @Override public void showFab(boolean show) {
    if (show) {
      fabSubscription = RxViewAnimation.animate(fab, R.anim.fade_in)
          .subscribe(viewAnimationEvent -> {
            fab.setVisibility(VISIBLE);
            fabShown = true;
          });

    } else {
      fabSubscription = RxViewAnimation.animate(fab, R.anim.fade_out)
          .subscribe(viewAnimationEvent -> {
            fab.setVisibility(INVISIBLE);
            fabShown = false;
          });
    }
  }

  @Override public BaseController getController() {
    return controller;
  }

  @Override public BaseModel getSavedModelFromLayout() {
    return controller.getModel();
  }
}
