package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.ForgotPasswordResponse;
import in.appnow.astrobuddy.rest.request.RegisterMobileRequest;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 14/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class ForgotPasswordModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;


    public ForgotPasswordModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<ForgotPasswordResponse> forgotPassword(RegisterMobileRequest registerMobileRequest) {
        return apiInterface.forgotPassword(registerMobileRequest);
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
}
