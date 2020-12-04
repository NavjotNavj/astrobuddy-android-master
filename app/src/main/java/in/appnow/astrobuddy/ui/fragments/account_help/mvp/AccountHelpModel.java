package in.appnow.astrobuddy.ui.fragments.account_help.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.AccountHelpResponse;
import io.reactivex.Observable;

/**
 * Created by sonu on 16:18, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AccountHelpModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public AccountHelpModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }
    public Observable<AccountHelpResponse> getAccountHelpInfo() {
        return apiInterface.getMyAccountHelpInfo();
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
