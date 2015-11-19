package co.vgetto.har.ui.historydetail;

import android.content.Context;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.MyApplication;
import co.vgetto.har.R;
import co.vgetto.har.Rx;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.SavedFile;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.di.modules.HistoryDetailModule;
import co.vgetto.har.di.modules.TriggerListModule;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseLayout;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.historylist.HistoryAdapter;
import co.vgetto.har.ui.rxanimation.RxViewAnimation;
import co.vgetto.har.ui.triggerlist.TriggerAdapter;
import co.vgetto.har.ui.triggerlist.TriggerListController;
import com.jakewharton.rxbinding.view.RxView;
import java.util.List;
import javax.inject.Inject;
import org.w3c.dom.Text;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 25.9.2015..
 */
public class HistoryDetailLayout extends FrameLayout
    implements HistoryDetailController.ITalkToHistoryDetailLayout,
    HistoryDetailAdapter.FileListClicks, BaseLayout {
  @Inject Context context;

  @Inject HistoryDetailController controller;

  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  @Bind(R.id.fabBtn) FloatingActionButton fab;

  private BaseModel historyDetailModel;

  private LayoutInflater inflater;

  private LinearLayoutManager linearLayoutManager;

  private Parcelable listSaveInstance;

  public HistoryDetailLayout(Context context, BaseModel model) {
    this(context, null, model);
  }

  public HistoryDetailLayout(Context context, AttributeSet attrs, BaseModel model) {
    this(context, attrs, 0, model);
  }

  public HistoryDetailLayout(Context context, AttributeSet attrs, int defStyleAttr,
      BaseModel model) {
    super(context, attrs, defStyleAttr);
    this.historyDetailModel = model;

    MyApplication.get(getContext())
        .getMainActivityComponent()
        .plus(new HistoryDetailModule(this))
        .inject(this);

    inflater = LayoutInflater.from(context);
    inflateLayout();
    initRecyclerView();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    controller.init(historyDetailModel);
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
    recyclerView.setAdapter(new HistoryDetailAdapter(this, null));
  }

  // ITalkToTriggerListLayout interface implementation

  @Override public BaseController getController() {
    return controller;
  }

  @Override public BaseModel getSavedModelFromLayout() {
    return controller.getModel();
  }

  @Override public void cardClicked(SavedFile file) {

  }

  @Override public void setAdapterData(List<SavedFile> fileList) {
    listSaveInstance = recyclerView.getLayoutManager().onSaveInstanceState();//save
    recyclerView.setAdapter(new HistoryDetailAdapter(this, fileList));
    recyclerView.getLayoutManager().onRestoreInstanceState(listSaveInstance);//restore
  }
}
