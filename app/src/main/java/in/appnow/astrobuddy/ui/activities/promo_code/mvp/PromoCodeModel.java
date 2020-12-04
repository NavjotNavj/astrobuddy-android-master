package in.appnow.astrobuddy.ui.activities.promo_code.mvp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.ApplyPromoCodeRequest;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.rest.request.PromoCodeRequest;
import in.appnow.astrobuddy.rest.response.PromoCodeResponse;
import io.reactivex.Observable;

import static android.app.Activity.RESULT_OK;
import static in.appnow.astrobuddy.ui.activities.promo_code.PromoCodeActivity.EXTRA_AMOUNT;
import static in.appnow.astrobuddy.ui.activities.promo_code.PromoCodeActivity.EXTRA_PROMO_TYPE;
import static in.appnow.astrobuddy.ui.activities.promo_code.PromoCodeActivity.EXTRA_TOPIC_COUNT;

/**
 * Created by sonu on 15:04, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodeModel {
    public static final String EXTRA_PROMO_DATA = "promo_data";
    public static final String EXTRA_PROMO_CODE = "promo_code";
    public static final String EXTRA_PROMO_ID = "promo_id";

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public PromoCodeModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public String getPromoType() {
        return appCompatActivity.getIntent().getStringExtra(EXTRA_PROMO_TYPE);
    }

    public int getTopicsCount() {
        return appCompatActivity.getIntent().getIntExtra(EXTRA_TOPIC_COUNT, 0);
    }

    public double getAmount() {
        return appCompatActivity.getIntent().getDoubleExtra(EXTRA_AMOUNT, 0);
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

    public Observable<PromoCodeResponse> fetchPromoCodes(PromoCodeRequest request) {
        return apiInterface.getPromoCodes(request);
    }

    public Observable<ApplyPromoCodeResponse> applyPromoCode(ApplyPromoCodeRequest request) {
        return apiInterface.applyPromoCode(request);
    }

    public void onPromoCodeAppliedSuccessfully(ApplyPromoCodeResponse response, String promoCode,int promoId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PROMO_DATA, response);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PROMO_CODE, promoCode);
        intent.putExtra(EXTRA_PROMO_ID,promoId);
        intent.putExtras(bundle);
        appCompatActivity.setResult(RESULT_OK, intent);
        appCompatActivity.finish();
    }
}
