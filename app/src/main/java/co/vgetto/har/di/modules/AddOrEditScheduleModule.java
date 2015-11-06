package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.ViewScope;
import co.vgetto.har.rxservices.RxScheduleService;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.addoredit.base.ITalkToAddEditLayout;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleController;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditSchedulePresenter;
import dagger.Module;
import dagger.Provides;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class AddOrEditScheduleModule {
  private ITalkToAddEditLayout iTalkToAddEditLayout;

  public AddOrEditScheduleModule(ITalkToAddEditLayout iTalkToAddEditLayout) {
    this.iTalkToAddEditLayout = iTalkToAddEditLayout;
  }

  @Provides @ViewScope AddOrEditSchedulePresenter providesAddOrEditSchedulePresenter() {
    return new AddOrEditSchedulePresenter();
  }

  @Provides @ViewScope AddOrEditScheduleController providesAddOrEditScheduleController(MainActivityController mainActivityController, AddOrEditSchedulePresenter presenter, Calendar calendar, Date date, RxScheduleService rxScheduleService) {
   return new AddOrEditScheduleController(mainActivityController, iTalkToAddEditLayout, presenter, calendar, date, rxScheduleService);
  }
}
