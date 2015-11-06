package co.vgetto.har.di.components;

import co.vgetto.har.db.entities.History;
import co.vgetto.har.di.modules.AddOrEditScheduleModule;
import co.vgetto.har.di.modules.AddOrEditTriggerModule;
import co.vgetto.har.di.modules.HistoryDetailModule;
import co.vgetto.har.di.modules.HistoryListModule;
import co.vgetto.har.di.modules.MainActivityModule;
import co.vgetto.har.di.modules.ScheduleListModule;
import co.vgetto.har.di.modules.TriggerListModule;
import co.vgetto.har.di.scopes.MainActivityScope;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.backstack.Backstack;
import co.vgetto.har.ui.MainActivity;
import co.vgetto.har.ui.MainActivityPresenter;
import dagger.Subcomponent;
/**
 * Created by Kovje on 23.8.2015..
 */
@MainActivityScope @Subcomponent(
    modules = {
        MainActivityModule.class
    }) public interface MainActivityComponent {

  void inject(MainActivity mainActivity);

  MainActivityController.ITalkToMainActivity italktomainactivity();

  MainActivityPresenter presenter();

  MainActivityController controller();

  Backstack backstack();

  ScheduleListComponent plus(ScheduleListModule scheduleListModule);

  TriggerListComponent plus(TriggerListModule triggerListModule);

  HistoryListComponent plus(HistoryListModule historyListModule);

  HistoryDetailComponent plus(HistoryDetailModule historyDetailModule);

  AddOrEditScheduleComponent plus(AddOrEditScheduleModule addOrEditScheduleModule);

  AddOrEditTriggerComponent plus(AddOrEditTriggerModule addOrEditTriggerModule);

}
