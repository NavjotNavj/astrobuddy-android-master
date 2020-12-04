package in.appnow.astrobuddy.ui.activities.payment_result.mvp;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.ui.activities.payment_result.PaymentResultActivity;

/**
 * Created by sonu on 17:28, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PaymentResultModel {

    private final AppCompatActivity appCompatActivity;

    public PaymentResultModel(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public String getOrderId() {
        return appCompatActivity.getIntent().getStringExtra(PaymentResultActivity.ARG_ORDER_ID);
    }

    public String getTrackingId() {
        return appCompatActivity.getIntent().getStringExtra(PaymentResultActivity.ARG_TRACKING_ID);
    }

    public String getStatus() {
        return appCompatActivity.getIntent().getStringExtra(PaymentResultActivity.ARG_STATUS);
    }

    public String getStatusMessage() {
        return appCompatActivity.getIntent().getStringExtra(PaymentResultActivity.ARG_STATUS_MESSAGE);
    }

    public String getPaymentType() {
        return appCompatActivity.getIntent().getStringExtra(PaymentResultActivity.PAYMENT_TYPE);
    }

    public boolean getErrorStatus() {
        return appCompatActivity.getIntent().getBooleanExtra(PaymentResultActivity.ARG_ERROR_STATUS, true);
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

    public void closeActivity() {
        Intent intent = new Intent();
        appCompatActivity.setResult(Activity.RESULT_OK, intent);
        appCompatActivity.finish();
    }
}
