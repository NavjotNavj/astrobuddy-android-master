package in.appnow.astrobuddy.ui.fragments.panchang.input.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;

/**
 * Created by sonu on 11:49, 17/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PanchangInputModel {
    private final AppCompatActivity appCompatActivity;

    public PanchangInputModel(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
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
