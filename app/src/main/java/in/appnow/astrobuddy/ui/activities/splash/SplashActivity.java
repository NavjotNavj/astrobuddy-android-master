package in.appnow.astrobuddy.ui.activities.splash;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.jobs.TipOfTheDailyAlarm;
import in.appnow.astrobuddy.ui.activities.splash.dagger.DaggerSplashActivityComponent;
import in.appnow.astrobuddy.ui.activities.splash.dagger.SplashActivityModule;
import in.appnow.astrobuddy.ui.activities.splash.mvp.SplashPresenter;
import in.appnow.astrobuddy.ui.activities.splash.mvp.SplashView;

public class SplashActivity extends BaseActivity {
    @Inject
    SplashView view;
    @Inject
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSplashActivityComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .splashActivityModule(new SplashActivityModule(this))
                .build().inject(this);
        setContentView(view);
        configFireBaseRemote();
        presenter.onCreate();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    /*  ==================      FIREBASE REMOTE CONFIG       ==================  */
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    /**
     * config firebase
     */
    private void configFireBaseRemote() {
        // Get Remote Config instance.
        // [START get_remote_config_instance]
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // [END get_remote_config_instance]

        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development. See Best Practices in the
        // README for more information.
        // [START enable_dev_mode]

       /* FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);*/


        // [END enable_dev_mode]

        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        // [END set_default_values]

        fetchRemoteConfig();


    }

    /**
     * fetches latest remote config
     */
    private void fetchRemoteConfig() {

        long cacheExpiration = 6 * 3600; // 6 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // After config data is successfully fetched, it must be activated before newly fetched
                        // values are returned.
                        mFirebaseRemoteConfig.activateFetched();
                    } else {

                    }
                });
        // [END fetch_config_with_callback]
    }
}
