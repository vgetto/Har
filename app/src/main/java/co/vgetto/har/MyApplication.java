package co.vgetto.har;

import android.app.Application;
import android.content.Context;
import co.vgetto.har.di.components.AppComponent;
import co.vgetto.har.di.components.DaggerAppComponent;
import co.vgetto.har.di.components.MainActivityComponent;
import co.vgetto.har.di.modules.AppModule;
import co.vgetto.har.di.modules.MainActivityModule;
import co.vgetto.har.ui.MainActivity;
import com.facebook.stetho.Stetho;
import timber.log.Timber;

/**
 * Created by Kovje on 18.8.2015..
 */
public class MyApplication extends Application {
  private volatile AppComponent appComponent;
  private MainActivityComponent mainActivityComponent;

  public static MyApplication get(Context context) {
    return (MyApplication) context.getApplicationContext();
  }

  public void onCreate() {
    super.onCreate();
    Stetho.initialize(Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        .build());

    Timber.plant(new Timber.DebugTree());
  }

  @Override public void onTerminate() {
    super.onTerminate();
  }

  public AppComponent getAppComponent() {
    if (appComponent == null) {
      synchronized (MyApplication.class) {
        if (appComponent == null) {
          appComponent = createAppComponent();
        }
      }
    }
    return appComponent;
  }

  private AppComponent createAppComponent() {
    return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
  }


  public MainActivityComponent createMainActivityComponent(MainActivity mainActivity) {
    if (mainActivityComponent == null) {
      mainActivityComponent = appComponent.plus(new MainActivityModule(mainActivity));
    }
    return mainActivityComponent;
  }

  public MainActivityComponent getMainActivityComponent() {
    return mainActivityComponent;
  }

  public void releaseMainActivityComponent() {
    this.mainActivityComponent = null;
  }
}
