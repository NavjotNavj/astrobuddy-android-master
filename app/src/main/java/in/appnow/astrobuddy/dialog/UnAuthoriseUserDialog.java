package in.appnow.astrobuddy.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.evernote.android.job.JobManager;
import com.razorpay.Checkout;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.utils.ToastUtils;

/**
 * Created by NILESH BHARODIYA on 04-09-2019.
 */
public class UnAuthoriseUserDialog {
    private static UnAuthoriseUserDialog ourInstance;
    private AlertDialog alertDialog;

    public interface OnLogoutListener {
        void OnClickLogOut();

        void OnLogOut();
    }

    public static UnAuthoriseUserDialog getInstance() {
        if (ourInstance == null) {
            ourInstance = new UnAuthoriseUserDialog();
        }

        return ourInstance;
    }

    private UnAuthoriseUserDialog() {
    }

    public void showLogOutDialog(Context context, String message) {

        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            alertDialog = null;
        }

        alertDialog = new AlertDialog.Builder(context, R.style.DialogTheme)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {

                })
                .setCancelable(false)
                .create();

        alertDialog.show();
    }

    public void showLogOutDialog(AppCompatActivity appCompatActivity, String message,
                                 PreferenceManger preferenceManger, ABDatabase abDatabase,
                                 OnLogoutListener onLogoutListener) {

        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            alertDialog = null;
        }

        alertDialog = new AlertDialog.Builder(appCompatActivity, R.style.DialogTheme)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    onLogoutListener.OnClickLogOut();

                    new Handler()
                            .postDelayed(() ->
                                    clearData(appCompatActivity,
                                            preferenceManger,
                                            abDatabase,
                                            onLogoutListener), 50);

                })
                .setCancelable(false)
                .create();

        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    private void clearData(AppCompatActivity context,
                           PreferenceManger preferenceManger,
                           ABDatabase abDatabase,
                           OnLogoutListener onLogoutListener) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                //Clear Razor pay data when user logout
                ((Activity) context).runOnUiThread(() -> Checkout.clearUserData(context));

                //delete chat table
                abDatabase.conversationDao().deleteChatTable();
                abDatabase.screenStatsDao().deleteTable();
                abDatabase.bannerStatsDao().deleteTable();

                //clear use session
                preferenceManger.logoutUser();

                //clear tip of the day
                preferenceManger.putString(PreferenceManger.TIP_OF_THE_DAY, "");

                //remove chatting related data like handler id, chat query and notify type
                preferenceManger.putString(PreferenceManger.HANDLER_ID, "");
                preferenceManger.putString(PreferenceManger.CHAT_QUERY, "");
                preferenceManger.putString(PreferenceManger.CHAT_SESSION_ID, "");
                preferenceManger.putInt(PreferenceManger.CONVERSATION_NOTIFY_TYPE, ConversationUtils.NOTIFY_NONE);
                preferenceManger.putPendingFeedback(null);
                preferenceManger.putSessionExpiredModel(null);

                //cancel/remove all notifications linked with app during logout
                NotificationUtils.cancelAllNotification();

                //cancel all running jobs
                try {
                    JobManager.instance().cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                ToastUtils.shortToast("Logout Success.");
                onLogoutListener.OnLogOut();
            }
        }.execute();

    }
}
