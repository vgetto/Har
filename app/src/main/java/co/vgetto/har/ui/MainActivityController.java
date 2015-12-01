package co.vgetto.har.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerController;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerLayout;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleController;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleLayout;

/**
 * Created by Kovje on 19.10.2015..
 */
public class MainActivityController implements BaseController{
  public final ITalkToMainActivity iTalkToMainActivity;
  private final MainActivityPresenter presenter;

  private BaseModel model;

  public MainActivityController(ITalkToMainActivity iTalkToMainActivity, MainActivityPresenter presenter) {
    this.iTalkToMainActivity = iTalkToMainActivity;
    this.presenter = presenter;
  }

  @Override public void init(BaseModel savedModel) {
    if (model != null) {
      this.model = savedModel;
    } else {
      this.model = new BaseModel();
    }
    iTalkToMainActivity.showLayout(UiConstants.SCHEDULES, UiConstants.ACTION_INIT, null);
  }


  @Override public BaseModel getModel() {
    return model;
  }

  public void logIn() { iTalkToMainActivity.login();}

  public void setTitle(String title) {
    iTalkToMainActivity.setTitle(title);
  }

  public boolean onNavigationItemSelected(int itemId) {
    iTalkToMainActivity.showLayout(presenter.getLayoutByItemId(itemId), UiConstants.ACTION_REPLACE, null);
    return false;
  }

  public boolean onBackPressed(int currentViewId, View currentView) {
    if (currentViewId == UiConstants.HISTORY_DETAILS) {
      iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK, null);
      return true;
    }
    else if (currentViewId == UiConstants.ADD_OR_EDIT_SCHEDULE) {
      AddOrEditScheduleLayout layout = (AddOrEditScheduleLayout) currentView;
      AddOrEditScheduleController controller = (AddOrEditScheduleController)layout.getController();
      if (!controller.onBackPressed()) {
        // controller of that layout didn't handle onBackPressed(), go back
        iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK, null);
      }
      // controller handled onBackpressed
      return true;
    } else if (currentViewId == UiConstants.ADD_OR_EDIT_TRIGGER) {
      AddOrEditTriggerLayout layout = (AddOrEditTriggerLayout) currentView;
      AddOrEditTriggerController controller = (AddOrEditTriggerController) layout.getController();
      if (!controller.onBackPressed()) {
        iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK, null);
      }
      return true;
    }
    // MainActivityController didn't handle onBackPressed, let android handle it
    return false;
  }

  public boolean onSupportNavigateUp() {
    iTalkToMainActivity.showLayout(-1, UiConstants.ACTION_BACK, null);
    return true;
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    /*
    if (requestCode == SettingsFragment.RC_SIGN_IN) {
      SettingsFragment fragment =
          (SettingsFragment) getSupportFragmentManager().findFragmentByTag("SettingsFragment");
      if (fragment != null) {
        fragment.onActivityResult(requestCode, resultCode, data);
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
    */
  }

  public Bundle onSaveInstanceState(Bundle outState) {
    /*
    if (currentView instanceof ConfigurationLayout) {
      return ((ConfigurationLayout) currentView).getPresenter().handleOnSaveInstanceState(outState);
    }
    */
    return outState;
  }


  public interface ITalkToMainActivity {
    void showLayout(int layoutId, int action, BaseModel model);
    void showSnackbar(String snackbarText);
    void setTitle(String title);
    void login();
    FragmentManager getFrManager();
  }

}
