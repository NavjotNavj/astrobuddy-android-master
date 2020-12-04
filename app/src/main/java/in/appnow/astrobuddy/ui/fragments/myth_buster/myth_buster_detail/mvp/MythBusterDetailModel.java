package in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.ui.activities.chat_feedback.ChatFeedbackActivity;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FileUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by sonu on 16:45, 02/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterDetailModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public MythBusterDetailModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public Observable<StartChatResponse> startChat() {
        return apiInterface.startChat();
    }

    public void showAddTopicDialog(String message) {
        DialogHelperClass.showMessageOKCancel(appCompatActivity, message, "Add Topic", "Cancel", (dialogInterface, i) -> {
            FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, MyAccountFragment.newInstance(), FragmentUtils.MY_ACCOUNT_FRAGMENT);
        }, null);
    }

    public void startConversationActivity(int currentQueue, String sessionId, boolean isExistingChat) {
        ConversationActivity.openActivity(appCompatActivity, currentQueue, sessionId, isExistingChat);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void openFeedbackActivity(String chatSessionId, String date) {
        ChatFeedbackActivity.openChatFeedbackActivity(appCompatActivity, chatSessionId, false, "Please give feedback for your previous chat on " + DateUtils.parseFeedBackDateFormat(date), FragmentUtils.FEEDBACK_TYPE_CHAT, -1);
    }

    public void onPermissionDenied() {
        ToastUtils.shortToast("You need storage permission to perform this action.");
    }

    public void onPermissionDeniedPermanently() {
        ToastUtils.shortToast("Please go to Setting and allow permissions.");

    }

    public Observable<ResponseBody> downloadImage(String source) {
        return apiInterface.downloadImage(source);
    }

    public void shareProduct(Bitmap bitmap, String message) {
        if (bitmap != null) {
            File file = FileUtils.SaveImage(bitmap, appCompatActivity, "");
            if (file != null) {
                Uri uri = FileProvider.getUriForFile(appCompatActivity, BuildConfig.FILES_AUTHORITY, file);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AstroBuddy Myth Buster");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                List<ResolveInfo> resInfoList = appCompatActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    appCompatActivity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                try {
                    appCompatActivity.startActivity(Intent.createChooser(intent, "Share Myth"));
                } catch (ActivityNotFoundException e) {
                    ToastUtils.shortToast("No app available to share.");
//                Toast.makeText(getActivity(), "No App Available", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


