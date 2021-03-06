package co.vgetto.har.di.components;

import co.vgetto.har.audio.RxAudioRecorder;
import co.vgetto.har.receivers.AlarmBroadcastReceiver;
import co.vgetto.har.db.DbModule;
import co.vgetto.har.di.modules.AppModule;
import co.vgetto.har.di.modules.MainActivityModule;
import co.vgetto.har.di.modules.RxServicesModule;
import co.vgetto.har.di.modules.NetworkModule;
import co.vgetto.har.di.scopes.ApplicationScope;
import co.vgetto.har.receivers.BaseCallReceiver;
import co.vgetto.har.receivers.BaseSMSReceiver;
import co.vgetto.har.syncadapter.SyncAdapter;
import co.vgetto.har.syncadapter.SyncObserver;
import co.vgetto.har.syncadapter.provider.HarProvider;
import co.vgetto.har.ui.settings.SettingsLayout;
import dagger.Component;

/**
 * Created by Kovje on 23.8.2015..
 */
@ApplicationScope @Component(
    modules = {
        AppModule.class, NetworkModule.class, RxServicesModule.class, DbModule.class
    }) public interface AppComponent {

  MainActivityComponent plus(MainActivityModule mainActivityModule);

  void inject(RxAudioRecorder rxAudioRecorder);

  void inject(SyncAdapter adapter);

  void inject(SyncObserver observer);

  void inject(AlarmBroadcastReceiver receiver);

  void inject(HarProvider provider);

  void inject(BaseCallReceiver baseCallReceiver);

  void inject(BaseSMSReceiver baseSMSReceiver);
}
