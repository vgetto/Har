package co.vgetto.har.di.modules;

import android.app.Application;
import co.vgetto.har.di.scopes.ApplicationScope;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 23.8.2015..
 */
@Module
public class NetworkModule {
    @Provides
    @ApplicationScope
    OkHttpClient providesOkHttpClient(){
        return new OkHttpClient();
    }

}
