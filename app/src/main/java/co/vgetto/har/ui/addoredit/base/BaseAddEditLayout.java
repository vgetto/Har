package co.vgetto.har.ui.addoredit.base;

import co.vgetto.har.ui.base.BaseLayout;

/**
 * Created by Kovje on 26.10.2015..
 */
public interface BaseAddEditLayout extends BaseLayout, ITalkToAddEditLayout {
  void setButtonSubscriptions();
  void setCurrentPageSubscription(int currentPage);
}
