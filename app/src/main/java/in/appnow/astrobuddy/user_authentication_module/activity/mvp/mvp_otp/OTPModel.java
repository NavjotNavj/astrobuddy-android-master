package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import in.appnow.astrobuddy.rest.request.RegisterMobileRequest;
import in.appnow.astrobuddy.rest.response.OTPResponseModel;
import in.appnow.astrobuddy.rest.request.RegistrationRequestModel;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 09/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class OTPModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;


    public OTPModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<OTPResponseModel> registerOTP(RegisterMobileRequest registerMobileRequest) {
        return apiInterface.sendOTP(registerMobileRequest);
    }
    public RegistrationRequestModel getRegisterRequestData(){
       return (RegistrationRequestModel) appCompatActivity.getIntent().getSerializableExtra("Register_Model");
    }

    public Observable<LoginResponseModel> registerUser(RegistrationRequestModel registrationRequestModel) {
        return apiInterface.registerUser(registrationRequestModel);
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

}
