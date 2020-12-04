package in.appnow.astrobuddy.dagger.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.dagger.scope.AppScope;
import in.appnow.astrobuddy.helper.PreferenceManger;

@Module
public class SharedPreferencesModule {
    private final PreferenceManger preferenceManger;

    public SharedPreferencesModule(Application application) {
        preferenceManger = new PreferenceManger(application.getSharedPreferences(AstroAppConstants.ASTRO_PREF_MANAGER, Context.MODE_PRIVATE));
    }

    @Provides
    @AppScope
    PreferenceManger provideSharedPreferences() {
        return preferenceManger;
    }
}
