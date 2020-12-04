package in.appnow.astrobuddy.ui.activities.splash.mvp;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by sonu on 12:39, 19/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SplashView extends BaseViewClass implements BaseView {

    @BindView(R.id.splash_center_logo)
    ImageView centerLogo;
    @BindView(R.id.background_image)
    ImageView backgroundImageView;

    public SplashView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        inflate(appCompatActivity, R.layout.activity_splash, this);
        ButterKnife.bind(this);
        ImageUtils.setBackgroundImage(appCompatActivity, backgroundImageView);
        ImageUtils.setDrawableImage(appCompatActivity, centerLogo, R.drawable.ic_logo_with_name);
    }

    @Override
    public void onUnknownError(String error) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {

    }


}
