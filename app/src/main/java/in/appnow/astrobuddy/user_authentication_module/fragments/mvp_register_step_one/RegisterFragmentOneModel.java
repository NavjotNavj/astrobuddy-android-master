package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterFragmentOneModel {

    private final AppCompatActivity appCompatActivity;


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

    public RegisterFragmentOneModel(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }
}
