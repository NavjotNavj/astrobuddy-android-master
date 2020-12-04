package in.appnow.astrobuddy.ui.activities.intro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.intro.dagger.DaggerIntroPagerActivityComponent;
import in.appnow.astrobuddy.ui.activities.intro.dagger.IntroPagerActivityComponent;
import in.appnow.astrobuddy.ui.activities.intro.dagger.IntroPagerActivityModule;
import in.appnow.astrobuddy.ui.activities.intro.mvp.IntroPagerPresenter;
import in.appnow.astrobuddy.ui.activities.intro.mvp.IntroPagerView;
import in.appnow.astrobuddy.ui.activities.promo_code.dagger.DaggerPromoCodeActivityComponent;
import in.appnow.astrobuddy.ui.activities.promo_code.dagger.PromoCodeModule;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.PromoCodePresenter;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.view.PromoCodeView;

/**
 * Created by sonu on 15:03, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class IntroPagerActivity extends AppCompatActivity {

    @Inject
    IntroPagerView view;
    @Inject
    IntroPagerPresenter presenter;

    private IntroPagerActivityComponent component;

    public static void startIntroActivity(Context context) {
        Intent intent = new Intent(context, IntroPagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = DaggerIntroPagerActivityComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .introPagerActivityModule(new IntroPagerActivityModule(this))
                .build();
        component.inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    public IntroPagerActivityComponent getComponent() {
        return component;
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
