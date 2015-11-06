package co.vgetto.har.ui.historydetail;

import android.content.Context;
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
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.di.modules.HistoryDetailModule;
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
import org.w3c.dom.Text;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 25.9.2015..
 */
public class HistoryDetailLayout extends FrameLayout implements
    HistoryDetailController.ITalkToHistoryDetailLayout, BaseLayout {
  @Inject HistoryDetailController controller;

  @Bind(R.id.tvHistoryDetail) TextView historyDetail;

  private BaseModel historyDetailModel;

  private LayoutInflater inflater;

  public HistoryDetailLayout(Context context, BaseModel model) {
    this(context, null, model);
  }

  public HistoryDetailLayout(Context context, AttributeSet attrs, BaseModel model) {
    this(context, attrs, 0, model);
  }

  public HistoryDetailLayout(Context context, AttributeSet attrs, int defStyleAttr, BaseModel model) {
    super(context, attrs, defStyleAttr);
    this.historyDetailModel = model;

    MyApplication.get(getContext())
        .getMainActivityComponent()
        .plus(new HistoryDetailModule(this))
        .inject(this);

    inflater = LayoutInflater.from(context);
    inflateLayout();
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
    inflater.inflate(R.layout.history_detail, this, true);
    ButterKnife.bind(this);
  }

  // ITalkToTriggerListLayout interface implementation

  @Override public BaseController getController() {
    return controller;
  }

  @Override public BaseModel getSavedModelFromLayout() {
    return controller.getModel();
  }

  @Override public void setData(History h) {
    historyDetail.setText(h.toString());
  }
}
