package in.appnow.astrobuddy.ui.fragments.myaccount.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.TransactionHistoryResponse;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.UpgradePlanFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 10:59, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MyAccountModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;


    public MyAccountModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<MyAccountResponse> getMyAccountDetails(BaseRequestModel baseRequestModel) {
        return apiInterface.getMyAccountDetails(baseRequestModel);
    }

    public Observable<TransactionHistoryResponse> getTransactionHistory(BaseRequestModel baseRequestModel) {
        return apiInterface.getTransactionHistory(baseRequestModel);
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

    public void replaceAddTopicPointsFragment(int topicsCount) {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, AddCreditsFragment.newInstance(topicsCount), FragmentUtils.ADD_CREDIT_FRAGMENT);
    }

    public void replaceUpgradePlanFragment() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, UpgradePlanFragment.newInstance(), FragmentUtils.UPGRADE_PLAN_FRAGMENT);
    }

    public APIInterface getApiInterface() {
        return apiInterface;
    }
}
