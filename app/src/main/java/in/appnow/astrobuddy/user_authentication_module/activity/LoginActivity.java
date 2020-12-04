package in.appnow.astrobuddy.user_authentication_module.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login.DaggerLoginComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login.LoginComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login.LoginModule;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_login.LoginPresenter;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_login.LoginView;
import in.appnow.astrobuddy.utils.FragmentUtils;

/**
 * Created by Abhishek Thanvi on 12/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class LoginActivity extends BaseActivity {

    @Inject
    LoginView loginView;
    @Inject
    LoginPresenter loginPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerLoginComponent.builder().appComponent(AstroApplication.get(this).component())
                .loginModule(new LoginModule(this))
                .build().inject(this);
        getLoginComponent();
        setContentView(loginView);
        loginPresenter.onCreate();

    }

    public LoginComponent getLoginComponent() {
        return DaggerLoginComponent
                .builder()
                .appComponent(AstroApplication.get(this).component())
                .loginModule(new LoginModule(this))
                .build();
    }

    @Override
    public void onBackPressed() {
        Fragment forgotPasswordTwoFragment = getSupportFragmentManager().
                findFragmentByTag(FragmentUtils.FORGOT_PASSWORD_TWO_FRAGMENT);

        if (forgotPasswordTwoFragment != null) {
            loginView.restartActivity();

        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}