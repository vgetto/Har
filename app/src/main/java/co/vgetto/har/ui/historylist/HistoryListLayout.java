package co.vgetto.har.ui.historylist;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.MyApplication;
import co.vgetto.har.R;
import co.vgetto.har.Rx;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.di.modules.HistoryListModule;
import co.vgetto.har.di.modules.TriggerListModule;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseLayout;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.rxanimation.RxViewAnimation;
import co.vgetto.har.ui.triggerlist.TriggerAdapter;
import co.vgetto.har.ui.triggerlist.TriggerListController;
import com.jakewharton.rxbinding.view.RxView;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 25.9.2015..
 */
public class HistoryListLayout extends FrameLayout implements HistoryListController.ITalkToHistoryListLayout, HistoryAdapter.HistoryListClicks,
    BaseLayout {
  @Inject Context context;

  @Inject HistoryListController controller;

  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  @Bind(R.id.fabBtn) FloatingActionButton fab;

  public LinearLayoutManager linearLayoutManager;

  private LayoutInflater inflater;


  public HistoryListLayout(Context context) {
    this(context, null);
  }

  public HistoryListLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HistoryListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    MyApplication.get(getContext())
        .getMainActivityComponent()
        .plus(new HistoryListModule(this))
        .inject(this);

    inflater = LayoutInflater.from(context);
    inflateLayout();
    initRecyclerView();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    controller.init(null);
  }

  @Override protected void onDetachedFromWindow() {
    controller.clearQuerySubscription();
    super.onDetachedFromWindow();
  }

  public void inflateLayout() {
    inflater.inflate(R.layout.recyclerview_fab_layout, this, true);
    ButterKnife.bind(this);
    RxView.visibility(fab).call(false);
  }

  public void initRecyclerView() {
    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    recyclerView.setHasFixedSize(true);

    // use a linear layout manager
    linearLayoutManager = new LinearLayoutManager(context);
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(new HistoryAdapter(this, null));
  }


  // ITalkToHistoryListLayout interface implementation


  @Override public BaseController getController() {
    return controller;
  }

  @Override public BaseModel getSavedModelFromLayout() {
    return controller.getModel();
  }


  @Override public void setAdapterData(List<History> history) {
    recyclerView.setAdapter(new HistoryAdapter(this, history));
  }

  @Override public void cardClicked(History h) {
    controller.openHistoryDetails(h);
    Timber.i("History clicked, inflate new layout, id -> " + Long.toString(h.id()));
  }
}
