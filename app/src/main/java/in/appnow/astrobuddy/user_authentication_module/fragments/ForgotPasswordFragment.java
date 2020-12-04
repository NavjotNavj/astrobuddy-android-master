package in.appnow.astrobuddy.user_authentication_module.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login.LoginComponent;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password.ForgotPasswordPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password.ForgotPasswordView;

/**
 * Created by Abhishek Thanvi on 14/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class ForgotPasswordFragment extends Fragment {

    @Inject
    public ForgotPasswordView forgotPasswordView;
    @Inject
    ForgotPasswordPresenter forgotPasswordPresenter;

    public static ForgotPasswordFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LoginComponent loginComponent = ((LoginActivity) getActivity()).getLoginComponent();
        loginComponent.inject(this);
        forgotPasswordPresenter.onCreate();
        return forgotPasswordView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        forgotPasswordPresenter.onDestroy();
    }
}
