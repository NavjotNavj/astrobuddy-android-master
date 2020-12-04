package in.appnow.astrobuddy.ui.activities.promo_code.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.PromoCodeModel;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.PromoCodePresenter;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.view.PromoCodeView;

/**
 * Created by sonu on 15:05, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class PromoCodeModule {
    private final AppCompatActivity appCompatActivity;

    public PromoCodeModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    /* MVP Injection for PromoCode Activity */
    @Provides
    @PromoCodeActivityScope
    public PromoCodeView promoCodeView() {
        return new PromoCodeView(appCompatActivity);
    }

    @Provides
    @PromoCodeActivityScope
    public PromoCodeModel promoCodeModel(APIInterface apiInterface) {
        return new PromoCodeModel(appCompatActivity, apiInterface);
    }

    @Provides
    @PromoCodeActivityScope
    public PromoCodePresenter promoCodePresenter(PromoCodeView view, PromoCodeModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new PromoCodePresenter(view, model,preferenceManger, abDatabase);
    }
}
