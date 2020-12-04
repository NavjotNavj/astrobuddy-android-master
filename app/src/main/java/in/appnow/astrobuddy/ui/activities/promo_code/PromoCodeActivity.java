package in.appnow.astrobuddy.ui.activities.promo_code;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.ui.activities.promo_code.dagger.DaggerPromoCodeActivityComponent;
import in.appnow.astrobuddy.ui.activities.promo_code.dagger.PromoCodeModule;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.PromoCodePresenter;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.view.PromoCodeView;

/**
 * Created by sonu on 15:03, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodeActivity extends AppCompatActivity {

    @Inject
    PromoCodeView view;
    @Inject
    PromoCodePresenter presenter;

    public static final String EXTRA_PROMO_TYPE = "promo_type";
    public static final String EXTRA_TOPIC_COUNT = "topic_count";
    public static final String EXTRA_AMOUNT = "amount";

    public static void startPromoCodeActivity(Context context, String promoType, int topicsCount, double amount, int requestCode) {
        Intent intent = new Intent(context, PromoCodeActivity.class);
        intent.putExtra(EXTRA_PROMO_TYPE, promoType);
        intent.putExtra(EXTRA_TOPIC_COUNT, topicsCount);
        intent.putExtra(EXTRA_AMOUNT, amount);
        ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerPromoCodeActivityComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .promoCodeModule(new PromoCodeModule(this))
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
