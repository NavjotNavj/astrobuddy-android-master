package in.appnow.astrobuddy.ui.fragments.changePassword.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.ChangePasswordRequest;
import in.appnow.astrobuddy.rest.response.ChangePasswordResponse;
import io.reactivex.Observable;

/**
 * Created by sonu on 10:59, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChangePasswordModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;


    public ChangePasswordModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }
    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }
    public Observable<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        return apiInterface.changePassword(changePasswordRequest);
    }
}
