package in.appnow.astrobuddy.jobs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

import in.appnow.astrobuddy.services.MyBroadCastReceiver;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 18:52, 2019-07-02
 * Copyright (c) 2019 . All rights reserved.
 */
public class TipOfTheDailyAlarm {

    public static void setAlarm(Context context) {
        // Set the alarm to start at approximately 7:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, "action.alarm");
        if (alarmManager != null) {
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000, getPendingIntent(context));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //20
                ///  alarmManager.set(AlarmManager.RTC_WAKEUP, 20 * 60 * 60 * 1000, pendingIntent);

                // With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
                // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(context));
                // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 20 * 60 * 60 * 1000, 24 * 60 * 60 * 1000, pendingIntent);
                //}
            }*/
            long time = calendar.getTimeInMillis();
            if (time < System.currentTimeMillis()) {
                time += AlarmManager.INTERVAL_DAY;
                Logger.ErrorLog("ALARMA", "UPDATE ALARM TIME");
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);

            Logger.ErrorLog("ALARMA", "SET");

        }
    }

    public static void cancelAlarm(Context context, String action) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getPendingIntent(context, action));
        }
    }

    public static void cancelTODAlarm(Context context) {
        cancelAlarm(context, "action.alarm");
    }

    public static void cancelJobAlarm(Context context) {
        cancelAlarm(context, "action.job");
    }

    private static PendingIntent getPendingIntent(Context context, String action) {
        Intent alarmIntent = new Intent(context, MyBroadCastReceiver.class);
        alarmIntent.setAction(action);
        // alarmIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        return pendingIntent;
    }

    public static void setJobAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 30);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context, "action.job");
        if (alarmManager != null) {
            long time = calendar.getTimeInMillis();
            if (time < System.currentTimeMillis()) {
                time += AlarmManager.INTERVAL_DAY;
                Logger.ErrorLog("ALARMA", "UPDATE JOB ALARM TIME");
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);

            Logger.ErrorLog("ALARMA", "JOB SET");
        }
    }

}
