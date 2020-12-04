package in.appnow.astrobuddy.ui.activities.splash.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.splash.mvp.SplashModel;
import in.appnow.astrobuddy.ui.activities.splash.mvp.SplashPresenter;
import in.appnow.astrobuddy.ui.activities.splash.mvp.SplashView;

/**
 * Created by sonu on 12:35, 19/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class SplashActivityModule {
    private final AppCompatActivity appCompatActivity;

    public SplashActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @SplashScope
    public SplashView view() {
        return new SplashView(appCompatActivity);
    }

    @Provides
    @SplashScope
    public SplashModel model(APIInterface apiInterface) {
        return new SplashModel(appCompatActivity, apiInterface);
    }

    @Provides
    @SplashScope
    public SplashPresenter presenter(SplashView view, SplashModel model, PreferenceManger preferenceManger) {
        return new SplashPresenter(view, model, preferenceManger);
    }

}
