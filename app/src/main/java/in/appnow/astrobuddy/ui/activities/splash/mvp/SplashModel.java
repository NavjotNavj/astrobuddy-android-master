package in.appnow.astrobuddy.ui.activities.splash.mvp;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.jobs.AppDownloadJob;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.AppVersionRequest;
import in.appnow.astrobuddy.rest.response.AppVersionResponse;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.activities.intro.IntroPagerActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.utils.AppUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 12:40, 19/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SplashModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public SplashModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    protected void launchActivity(boolean isLogin) {
        Intent intent;
        if (isLogin) {
            intent = new Intent(appCompatActivity, HomeActivity.class);
        } else {
            intent = new Intent(appCompatActivity, LoginActivity.class);

        }
        appCompatActivity.startActivity(intent);
        finishActivity();
    }

    public void finishActivity() {
        appCompatActivity.finish();
    }

    public Observable<AppVersionResponse> getAppVersion(AppVersionRequest request) {
        return apiInterface.getAppVersion(request);
    }

    public void startIntroActivity() {
        IntroPagerActivity.startIntroActivity(appCompatActivity);
        appCompatActivity.finish();
    }

    public void showAppUpgradeAlertDialog(String message, boolean isForceUpgrade, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity, R.style.DialogTheme);
        builder.setCancelable(false);
        builder.setTitle("Upgrade!!");
        builder.setMessage(message);
        builder.setPositiveButton("Upgrade", (dialogInterface, i) -> AppUtils.openPlayStore(appCompatActivity, BuildConfig.APPLICATION_ID));
        if (!isForceUpgrade) {
            builder.setNegativeButton("Continue", cancelListener);
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void sendAppDownload() {
        try {
            AppDownloadJob.schedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
