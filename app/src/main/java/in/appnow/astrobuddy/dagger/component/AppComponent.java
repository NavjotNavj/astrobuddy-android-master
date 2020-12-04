package in.appnow.astrobuddy.dagger.component;

import android.app.Service;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;

import dagger.Component;
import in.appnow.astrobuddy.dagger.module.AppModule;
import in.appnow.astrobuddy.dagger.module.NetworkModule;
import in.appnow.astrobuddy.dagger.module.RoomModule;
import in.appnow.astrobuddy.dagger.module.SharedPreferencesModule;
import in.appnow.astrobuddy.dagger.scope.AppScope;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.network.NetworkMonitor;
import in.appnow.astrobuddy.rest.APIInterface;
import okhttp3.OkHttpClient;

@AppScope
@Component(modules = {AppModule.class, NetworkModule.class, RoomModule.class, SharedPreferencesModule.class})
public interface AppComponent {

    Context context();

    APIInterface apiInterface();

    ABDatabase abDatabase();

    ViewModelProvider.Factory provideViewModelFactory();

    PreferenceManger preferenceManger();

}
