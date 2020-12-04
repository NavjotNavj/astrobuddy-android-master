package in.appnow.astrobuddy.ui.activities.payment_result.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.payment_result.mvp.PaymentResultModel;
import in.appnow.astrobuddy.ui.activities.payment_result.mvp.PaymentResultPresenter;
import in.appnow.astrobuddy.ui.activities.payment_result.mvp.PaymentResultView;

/**
 * Created by sonu on 17:25, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class PaymentResultModule {

    private final AppCompatActivity appCompatActivity;

    public PaymentResultModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @PaymentResultScope
    public PaymentResultView paymentResultView() {
        return new PaymentResultView(appCompatActivity);
    }

    @Provides
    @PaymentResultScope
    public PaymentResultModel paymentResultModel() {
        return new PaymentResultModel(appCompatActivity);
    }

    @Provides
    @PaymentResultScope
    public PaymentResultPresenter paymentResultPresenter(PaymentResultView view,
                                                         PaymentResultModel model,
                                                         PreferenceManger preferenceManger,
                                                         ABDatabase abDatabase) {
        return new PaymentResultPresenter(view, model, preferenceManger, abDatabase);
    }
}
