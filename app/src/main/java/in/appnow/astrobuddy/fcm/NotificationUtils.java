package in.appnow.astrobuddy.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.rest.CustomSSLSocketFactory;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.rest.SslUtils;
import in.appnow.astrobuddy.utils.Logger;


/**
 * Created by Sajeev on 06-04-2017.
 */
public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();
    public static String CHAT_UPDATES_CHANNEL = "chat_updates";
    public static String GENERAL_UPDATES_CHANNEL = "general_updates";
    public static String PROMOTIONAL_CHANNEL = "promotional_channel";

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, Intent intent, int notificationID, int icon, String channelId, int largeIcon) {
        showNotificationMessage(title, message, intent, null, notificationID, icon, channelId, largeIcon);
    }

    public void showNotificationMessage(final String title, final String message, Intent intent, String imageUrl, int notificationID, int icon, String channelId, int largeIcon) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, channelId);
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!TextUtils.isEmpty(imageUrl)) {
            Logger.DebugLog(TAG, "Image URL Not empty");
            //https://img.youtube.com/vi/%@/mqdefault.jpg
            Bitmap bitmap;
            if (!imageUrl.contains("/")) {
                bitmap = getBitmapFromURLYT(String.format("https://img.youtube.com/vi/%s/mqdefault.jpg", imageUrl));
            } else {
                bitmap = getBitmapFromURL( imageUrl);
            }
            Logger.DebugLog(TAG, "Image URL" +imageUrl);

            if (bitmap != null) {
                Logger.DebugLog(TAG, "Bitmap is not null");

                showBigNotification(bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID, largeIcon);
            } else {
                Logger.DebugLog(TAG, "Bitmap is null");

                showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID, largeIcon);
            }

        } else {
            showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound, notificationID, largeIcon);
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound, int notificationID, int largeIcon) {

        //Multiline notification style
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(message);

        Notification notification = mBuilder
                .setSmallIcon(icon)
                .setTicker(title)
                .setContentTitle(title)

                .setContentText(message)
                .setGroup("Astrobuddy")
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setStyle(bigTextStyle)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIcon))
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }


    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound, int notificationID, int largeIcon) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder
                .setContentTitle(title)
                .setSmallIcon(icon)
                .setTicker(title)
                .setContentText(message)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIcon))
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(SslUtils.INSTANCE.getSslContextForCertificateFile(mContext).getSocketFactory());
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap getBitmapFromURLYT(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // Author: silentnuke

    // Playing notification sound

    /**
     * Method checks if the com.firebasenotification.app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotification(Context context, int notificationID) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.cancel(notificationID);
    }

    // Clears notification tray messages
    public static void clearSingleNotification(int notificationId) {
        NotificationManager notificationManager = (NotificationManager) AstroApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.cancel(notificationId);
    }

    // Clears notification tray messages
    public static void cancelAllNotification() {
        NotificationManager notificationManager = (NotificationManager) AstroApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.cancelAll();
    }



    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

}
