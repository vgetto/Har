package co.vgetto.har.ui.addoredit.base;

/**
 * Created by Kovje on 26.10.2015..
 */
public interface ITalkToAddEditLayout {
  // call for enabling/disabling of back and next buttons when add/edit schedule/trigger
  boolean setButtonsEnabled(boolean back, boolean next);
  // set page in add/edit schedule/trigger
  // provide int for current page, and action from UiConstants
  void setPage(int currentPage, int action);
}
