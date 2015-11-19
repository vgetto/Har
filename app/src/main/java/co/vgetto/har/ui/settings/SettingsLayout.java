package co.vgetto.har.ui.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.vgetto.har.MyApplication;
import co.vgetto.har.R;
import co.vgetto.har.Rx;
import co.vgetto.har.di.modules.ScheduleListModule;
import co.vgetto.har.di.modules.SettingsModule;
import co.vgetto.har.rxservices.RxUserService;
import co.vgetto.har.ui.MainActivity;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseLayout;
import co.vgetto.har.ui.base.BaseModel;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import javax.inject.Inject;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Kovje on 11.10.2015..
 */
public class SettingsLayout extends LinearLayout
    implements SettingsController.ITalkToSettingsLayout, BaseLayout {
  @Inject SettingsController controller;

  @Bind(R.id.tvLoggedIn) TextView tvLoggedIn;

  @Bind(R.id.btnLogin) Button btnLogin;

  public SettingsLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    MyApplication.get(getContext())
        .getMainActivityComponent()
        .plus(new SettingsModule(this))
        .inject(this);
    LayoutInflater.from(context).inflate(R.layout.settings_layout, this, true);
    ButterKnife.bind(this);
  }

  public SettingsLayout(Context context) {
    super(context);
    LayoutInflater.from(context).inflate(R.layout.settings_layout, this, true);
  }

  public SettingsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    controller.init(null);
    btnLogin.setOnClickListener(v -> controller.btnLoginClicked());
  }

  @Override protected void onDetachedFromWindow() {
    controller.clearQuerySubscription();
    super.onDetachedFromWindow();
  }

  @Override public BaseController getController() {
    return controller;
  }

  @Override public BaseModel getSavedModelFromLayout() {
    return controller.getModel();
  }

  @Override public void setData(String tvData, String btnText) {
    tvLoggedIn.setText(tvData);
    btnLogin.setText(btnText);
  }
}
