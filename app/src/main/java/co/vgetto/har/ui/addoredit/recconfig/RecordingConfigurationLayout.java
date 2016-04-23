package co.vgetto.har.ui.addoredit.recconfig;

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
import co.vgetto.har.db.entities.ConfigurationValue;
import co.vgetto.har.di.modules.RecordingConfigurationModule;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseLayout;
import co.vgetto.har.ui.base.BaseModel;
import com.jakewharton.rxbinding.view.RxView;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Created by Kovje on 25.9.2015..
 */
public class RecordingConfigurationLayout extends FrameLayout implements
    RecordingConfigurationController.ITalkToRecordingConfigurationLayout, RecordingConfigurationAdapter.RecordingConfigurationListClicks,
    BaseLayout {
  @Inject Context context;

  @Inject RecordingConfigurationController controller;

  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  @Bind(R.id.fabBtn) FloatingActionButton fab;

  public LinearLayoutManager linearLayoutManager;

  private LayoutInflater inflater;

  public RecordingConfigurationLayout(Context context) {
    this(context, null);
  }

  public RecordingConfigurationLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RecordingConfigurationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    MyApplication.get(getContext())
        .getMainActivityComponent()
        .plus(new RecordingConfigurationModule(this))
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
  }

  public Pair<Float, Float> getFabPosition() {
    return new Pair<Float, Float>(fab.getX(), fab.getY());
  }

  public void initRecyclerView() {
    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    recyclerView.setHasFixedSize(true);
    RxView.enabled(fab).call(false);
    // use a linear layout manager
    linearLayoutManager = new LinearLayoutManager(context);
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(new RecordingConfigurationAdapter(this, null));
  }

  // schedule list clicks interface method implementations

  // ITalkToScheduleListLayout interface implementation

  @Override public BaseController getController() {
    return controller;
  }

  @Override public BaseModel getSavedModelFromLayout() {
    return controller.getModel();
  }

  @Override public void setAdapterData(List<List<ConfigurationValue>> configurationValuesList) {
    recyclerView.setAdapter(new RecordingConfigurationAdapter(this, configurationValuesList));
  }

  @Override public void valueClicked(ConfigurationValue value) {
    Timber.i("Value clicked -> " + value.toString());
  }

  @Override public void addClicked(ConfigurationValue value) {

  }
}
