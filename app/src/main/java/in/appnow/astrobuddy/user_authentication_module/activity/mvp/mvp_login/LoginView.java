package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_login;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.user_authentication_module.fragments.LoginFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by Abhishek Thanvi on 12/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


@SuppressLint("ViewConstructor")
public class LoginView extends BaseViewClass {

    @BindView(R.id.background_image)
    ImageView backgroundImage;

    AppCompatActivity appCompatActivity;
    private final PreferenceManger preferenceManger;

    public LoginView(@NonNull AppCompatActivity appCompatActivity, PreferenceManger preferenceManger) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.preferenceManger = preferenceManger;
        inflate(getContext(), R.layout.activity_login, this);
        ButterKnife.bind(this);
        ImageUtils.setBackgroundImage(appCompatActivity, backgroundImage);
        goToLoginFragment();
    }

    public void restartActivity() {
        appCompatActivity.startActivity(new Intent(appCompatActivity, LoginActivity.class));
        appCompatActivity.finish();
    }

    public void goToLoginFragment() {
        FragmentUtils.replaceFragmentCommon(appCompatActivity.getSupportFragmentManager(), R.id.frame, LoginFragment.newInstance(), FragmentUtils.LOGIN_FRAGMENT, false);
    }


}
