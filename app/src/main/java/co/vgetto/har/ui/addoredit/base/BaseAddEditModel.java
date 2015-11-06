package co.vgetto.har.ui.addoredit.base;

import co.vgetto.har.ui.base.BaseModel;

/**
 * Created by Kovje on 30.10.2015..
 */
public class BaseAddEditModel extends BaseModel {
  public static final int LAYOUT_TYPE_ADD = 0;
  public static final int LAYOUT_TYPE_EDIT = 1;

  private int layoutType;
  private int currentPage;
  private boolean modelRestored;

  public int getLayoutType() {
    return layoutType;
  }

  public void setLayoutType(int layoutType) {
    this.layoutType = layoutType;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public boolean isModelRestored() {
    return modelRestored;
  }

  public void setModelRestored(boolean modelRestored) {
    this.modelRestored = modelRestored;
  }

  public int nextPage() {
    currentPage++;
    return currentPage;
  }

  public int previousPage() {
    currentPage--;
    return currentPage;
  }
}
