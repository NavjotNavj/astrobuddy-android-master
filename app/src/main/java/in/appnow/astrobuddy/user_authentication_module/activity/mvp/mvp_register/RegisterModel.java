package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.RegisterMobileRequest;
import in.appnow.astrobuddy.rest.response.OTPResponseModel;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;


    public RegisterModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;

        this.apiInterface = apiInterface;
    }


    public Observable<OTPResponseModel> registerOTP(RegisterMobileRequest registerMobileRequest) {
            return apiInterface.sendOTP(registerMobileRequest);
    }

    public Bundle getPreData(){
        return appCompatActivity.getIntent().getExtras();
    }


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }
}
