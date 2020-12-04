package in.appnow.astrobuddy.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.evernote.android.job.JobConfig;
import com.evernote.android.job.JobManager;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.libraries.places.api.Places;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.dagger.component.DaggerAppComponent;
import in.appnow.astrobuddy.dagger.module.AppModule;
import in.appnow.astrobuddy.dagger.module.RoomModule;
import in.appnow.astrobuddy.dagger.module.SharedPreferencesModule;
import in.appnow.astrobuddy.jobs.AstroJobCreator;
import in.appnow.astrobuddy.network.CheckInternetConnection;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

import static in.appnow.astrobuddy.fcm.NotificationUtils.CHAT_UPDATES_CHANNEL;
import static in.appnow.astrobuddy.fcm.NotificationUtils.GENERAL_UPDATES_CHANNEL;
import static in.appnow.astrobuddy.fcm.NotificationUtils.PROMOTIONAL_CHANNEL;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class AstroApplication extends MultiDexApplication {

    // This flag should be set to true to enable VectorDrawable support for API < 21
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static AstroApplication instance;
    private AppComponent appComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        instance = this;

        try {
            JobManager.create(this).addJobCreator(new AstroJobCreator());
            JobConfig.setLogcatEnabled(BuildConfig.DEBUG);
        } catch (Exception e) {
            // Crashlytics.logException(e);
            e.printStackTrace();
        }

//        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET))
                .debug(BuildConfig.DEBUG)//enable debug mode
                .build();
        Twitter.initialize(config);

        // This line needs to be executed before any usage of EmojiTextView, EmojiEditText or EmojiButton.
        EmojiManager.install(new GoogleEmojiProvider());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Verdana.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(this))
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), RestUtils.getPlacesAPIKey());
        }
    }

    public static AstroApplication getInstance() {
        return instance;
    }

    public static AstroApplication get(AppCompatActivity activity) {
        return (AstroApplication) activity.getApplication();
    }

    public static AstroApplication get(Service service) {
        return (AstroApplication) service.getApplication();
    }


    public AppComponent component() {
        return appComponent;
    }


    public boolean isInternetConnected(boolean showToast) {
        if (new CheckInternetConnection(this).isConnected())
            return true;
        else {
            if (showToast)
                ToastUtils.longToast(R.string.no_internet_connection);
            return false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// The user-visible name of the channel.
        CharSequence name = "Chat Updates";
// The user-visible description of the channel.
        String description = "This channel is use to send you chat updates regarding this app.";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHAT_UPDATES_CHANNEL, name, importance);
// Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        //mChannel.setShowBadge(false);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);


        // The user-visible name of the channel.
        CharSequence generalUpdates = "General Updates";
// The user-visible description of the channel.
        String generalUpdateDescription = "This channel is use to send you general app notifications.";
        NotificationChannel generalNotificationChannel = new NotificationChannel(GENERAL_UPDATES_CHANNEL, generalUpdates, importance);
// Configure the notification channel.
        generalNotificationChannel.setDescription(generalUpdateDescription);
        generalNotificationChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        generalNotificationChannel.setLightColor(Color.RED);
        generalNotificationChannel.enableVibration(true);
        //mChannel.setShowBadge(false);
        generalNotificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(generalNotificationChannel);

        // The user-visible name of the channel.
        CharSequence promotionName = "Promotions Updates";
// The user-visible description of the channel.
        String promotionDescription = "This channel is use to send you notifications about app promotions.";
        NotificationChannel promotionsNotificationChannel = new NotificationChannel(PROMOTIONAL_CHANNEL, promotionName, importance);
// Configure the notification channel.
        promotionsNotificationChannel.setDescription(promotionDescription);
        promotionsNotificationChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        promotionsNotificationChannel.setLightColor(Color.RED);
        promotionsNotificationChannel.enableVibration(true);
        //mChannel.setShowBadge(false);
        promotionsNotificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(promotionsNotificationChannel);
    }
}
