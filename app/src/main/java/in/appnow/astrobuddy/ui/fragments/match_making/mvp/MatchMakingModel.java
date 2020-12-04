package in.appnow.astrobuddy.ui.fragments.match_making.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;

/**
 * Created by sonu on 12:27, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public MatchMakingModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
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
}
