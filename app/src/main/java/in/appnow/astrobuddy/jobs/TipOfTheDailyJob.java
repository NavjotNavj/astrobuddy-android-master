package in.appnow.astrobuddy.jobs;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.dagger.component.DaggerMyJobsComponent;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;

/**
 * Created by sonu on 16/03/18.
 */

public final class TipOfTheDailyJob extends DailyJob {

    public static final String TAG = "TODDailyJob";

    @Inject
    PreferenceManger preferenceManger;

    // start this job when user is logged in
    public static void schedule(Context context) {
       // TipOfTheDailyAlarm.setAlarm(context);
        try {

            //cancel job if already running
            if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
                JobUtils.cancelJob(TAG);
            }
            // schedule between 7 and 8 AM
            //  DailyJob.schedule(new JobRequest.Builder(TAG), TimeUnit.HOURS.toMillis(FirebaseRemoteConfig.getInstance().getLong("tod_daily_notification_start_time")), TimeUnit.HOURS.toMillis(FirebaseRemoteConfig.getInstance().getLong("tod_daily_notification_end_time")));
            DailyJob.schedule(new JobRequest.Builder(TAG), TimeUnit.HOURS.toMillis(9), TimeUnit.HOURS.toMillis(10));
        } catch (Exception ignored) {

        }
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        DaggerMyJobsComponent.builder()
                .appComponent(AstroApplication.getInstance().component())
                .build()
                .inject(this);
        //show notification when tip of the day toggle is on
        if (preferenceManger.getBooleanValue(getContext().getResources().getString(R.string.tip_of_the_day), true)) {
            //show notification
            Intent resultIntent = new Intent(getContext(), HomeActivity.class);
            resultIntent.putExtra(Config.MESSAGE_TYPE, Config.TOD_APP_NOTIFICATION);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            NotificationUtils notificationUtils = new NotificationUtils(getContext());
            notificationUtils.showNotificationMessage("Today's Tip & Forecast", "Find your tip of the day and daily forecast. Click here.", resultIntent, Config.TOD_NOTIFICATION_ID, R.drawable.ic_launcher_notification, NotificationUtils.GENERAL_UPDATES_CHANNEL, R.mipmap.ic_tod);
        }

        return DailyJobResult.SUCCESS;
    }
}
