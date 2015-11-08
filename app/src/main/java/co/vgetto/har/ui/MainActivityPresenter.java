package co.vgetto.har.ui;

import co.vgetto.har.R;
import timber.log.Timber;

/**
 * Created by Kovje on 25.9.2015..
 */
public class MainActivityPresenter {
  public MainActivityPresenter() {
  }

  public int getLayoutByItemId(int itemId) {
    Timber.i("Izvodim getLayoutByItemId na - " + Thread.currentThread().getName());
    int viewId;
    switch (itemId) {
      case R.id.navSchedule:
        viewId = UiConstants.SCHEDULES;
        break;
      case R.id.navTriggers:
        viewId = UiConstants.TRIGGERS;
        break;
      case R.id.navHistory:
        viewId = UiConstants.HISTORY;
        break;
      default:
        viewId = UiConstants.SETTINGS;
        break;
    }
    return viewId;
  }
}

