package in.appnow.astrobuddy.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.jobs.PostUserStatsJob;
import in.appnow.astrobuddy.jobs.TipOfTheDailyAlarm;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 18:54, 2019-07-02
 * Copyright (c) 2019 . All rights reserved.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.ErrorLog("ALARMA", "BROADCAST");
        if (intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                TipOfTheDailyAlarm.cancelTODAlarm(context);
                TipOfTheDailyAlarm.cancelJobAlarm(context);
                TipOfTheDailyAlarm.setAlarm(context);
                TipOfTheDailyAlarm.setJobAlarm(context);
            } else if (intent.getAction().equalsIgnoreCase("action.alarm")) {
                Logger.ErrorLog("ALARMA", "TRIGGER");
                Intent resultIntent = new Intent(context, HomeActivity.class);
                resultIntent.putExtra(Config.MESSAGE_TYPE, Config.TOD_APP_NOTIFICATION);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NotificationUtils notificationUtils = new NotificationUtils(context);
                notificationUtils.showNotificationMessage("Today's Tip & Forecast", "Find your tip of the day and daily forecast. Click here.", resultIntent, Config.TOD_NOTIFICATION_ID, R.drawable.ic_launcher_notification, NotificationUtils.GENERAL_UPDATES_CHANNEL, R.mipmap.ic_tod);
                // Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show()
            }
            else if (intent.getAction().equalsIgnoreCase("action.job")) {
                Logger.ErrorLog("ALARMA", "JOBBB TRIGGER");
                PostUserStatsJob.schedule();
            }
        }
    }
}
