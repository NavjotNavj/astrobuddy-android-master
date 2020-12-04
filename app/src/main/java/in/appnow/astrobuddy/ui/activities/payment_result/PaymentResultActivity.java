package in.appnow.astrobuddy.ui.activities.payment_result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.ui.activities.payment_result.dagger.DaggerPaymentResultComponent;
import in.appnow.astrobuddy.ui.activities.payment_result.dagger.PaymentResultModule;
import in.appnow.astrobuddy.ui.activities.payment_result.mvp.PaymentResultPresenter;
import in.appnow.astrobuddy.ui.activities.payment_result.mvp.PaymentResultView;

/**
 * Created by sonu on 17:25, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PaymentResultActivity extends BaseActivity {

    public static final String ARG_ERROR_STATUS = "error_status";
    public static final String ARG_STATUS = "status";
    public static final String ARG_STATUS_MESSAGE = "status_message";
    public static final String ARG_ORDER_ID = "order_id";
    public static final String ARG_TRACKING_ID = "tracking_id";
    public static final String PAYMENT_TYPE = "payment_type";

    @Inject
    PaymentResultView view;
    @Inject
    PaymentResultPresenter presenter;

    public static void openPaymentResultActivity(Context context, boolean errorStatus, String status, String statusMessage, String orderId, String trackingId,String paymentType, int requestCode) {
        Intent intent = new Intent(context, PaymentResultActivity.class);
        intent.putExtra(ARG_ERROR_STATUS, errorStatus);
        intent.putExtra(ARG_STATUS, status);
        intent.putExtra(ARG_STATUS_MESSAGE, statusMessage);
        intent.putExtra(ARG_ORDER_ID, orderId);
        intent.putExtra(ARG_TRACKING_ID, trackingId);
        intent.putExtra(PAYMENT_TYPE, paymentType);
        ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerPaymentResultComponent.builder()
                .appComponent(AstroApplication.getInstance().component())
                .paymentResultModule(new PaymentResultModule(this))
                .build().inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
