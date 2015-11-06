package co.vgetto.har.ui.backstack;

import android.view.View;
import co.vgetto.har.ui.UiConstants;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerLayout;
import co.vgetto.har.ui.base.BaseLayout;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.MainActivity;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleLayout;
import co.vgetto.har.ui.historydetail.HistoryDetailLayout;
import co.vgetto.har.ui.historylist.HistoryListLayout;
import co.vgetto.har.ui.schedulelist.ScheduleListLayout;
import co.vgetto.har.ui.settings.SettingsLayout;
import co.vgetto.har.ui.triggerlist.TriggerListLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovje on 10.10.2015..
 */
public class Backstack {
  //private final MainActivity mainActivity;
  private List<BackstackItem> backStack = new ArrayList<BackstackItem>();
  private int position;
  private View view;

  public Backstack() {

  }

  public void push(int viewId, View view) {
    backStack.add(BackstackItem.create(viewId, view));
  }

  public View pop() {
    position = backStack.size() - 1;
    view = backStack.get(position).view();
    backStack.remove(position);
    return view;
  }

  public void clear() {
    backStack.clear();
  }

  public View getCurrentView() {
    position = backStack.size() - 1;
    return backStack.get(position).view();
  }

  public int getCurrentViewId() {
    position = backStack.size() - 1;
    return backStack.get(position).viewId();
  }

  public int getSize() {
    return backStack.size();
  }

  private BaseModel getSavedModelFromView(View v) {
    BaseLayout layout = (BaseLayout) v;
    BaseModel savedModel = layout.getSavedModelFromLayout();
    if (savedModel == null) {
      savedModel = new BaseModel();
    }
    return savedModel;
  }

  public ArrayList<SavedBackstackItem> onSaveInstanceState() {
    ArrayList<SavedBackstackItem> savedBackstackItems =
        new ArrayList<SavedBackstackItem>(backStack.size());
    for (BackstackItem item : backStack) {
      savedBackstackItems.add(
          SavedBackstackItem.create(item.viewId(), getSavedModelFromView(item.view())));
    }
    return savedBackstackItems;
  }

  private View getViewFromSavedItem(MainActivity mainActivity, SavedBackstackItem item) {
    View v;
    switch (item.viewId()) {
      case UiConstants.SCHEDULES:
        v = new ScheduleListLayout(mainActivity, null);
        break;
      case UiConstants.TRIGGERS:
        v = new TriggerListLayout(mainActivity, null);
        break;
      case UiConstants.HISTORY:
        v = new HistoryListLayout(mainActivity, null);
        break;
      case UiConstants.HISTORY_DETAILS:
        v = new HistoryDetailLayout(mainActivity, item.model());
        break;
      case UiConstants.ADD_OR_EDIT_SCHEDULE:
        v = new AddOrEditScheduleLayout(mainActivity, item.model());
        break;
      case UiConstants.ADD_OR_EDIT_TRIGGER:
        v = new AddOrEditTriggerLayout(mainActivity, item.model());
        break;
      case UiConstants.SETTINGS:
        v = new SettingsLayout(mainActivity, null);
        break;
      default:
        v = new SettingsLayout(mainActivity, null);
        break;
    }
    return v;
  }

  public void restoreInstanceState(MainActivity mainActivity,
      ArrayList<SavedBackstackItem> savedBackstackItems) {
    clear();
    for (SavedBackstackItem item : savedBackstackItems) {
      backStack.add(BackstackItem.create(item.viewId(), getViewFromSavedItem(mainActivity, item)));
    }
  }
}
