package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.LoginRequestModel;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 16/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class LoginFragmentModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;


    public LoginFragmentModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<LoginResponseModel> loginUser(LoginRequestModel loginRequestModel) {
        return apiInterface.loginUser(loginRequestModel);
    }
}
