package co.vgetto.har.di.modules;

import co.vgetto.har.di.scopes.MainActivityScope;
import co.vgetto.har.ui.MainActivityController;
import co.vgetto.har.ui.backstack.Backstack;
import co.vgetto.har.ui.MainActivityPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module public class MainActivityModule {
  private MainActivityController.ITalkToMainActivity iTalkToMainActivity;

  public MainActivityModule(MainActivityController.ITalkToMainActivity iTalkToMainActivity) {
    this.iTalkToMainActivity = iTalkToMainActivity;
  }

  @Provides @MainActivityScope MainActivityController.ITalkToMainActivity providesITalkToMainActivity() {
    return iTalkToMainActivity;
  }

  @Provides @MainActivityScope MainActivityPresenter providesMainPresenter() {
    return new MainActivityPresenter();
  }

  @Provides @MainActivityScope MainActivityController providesMainActivityController(MainActivityPresenter presenter) {
    return new MainActivityController(iTalkToMainActivity, presenter);
  }

  @Provides @MainActivityScope Backstack providesBackstack() {
    return new Backstack();
  }

}
