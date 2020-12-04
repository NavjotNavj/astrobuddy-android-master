package in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_otp;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp.OTPModel;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp.OTPPresenter;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp.OTPVerificationActivityView;

/**
 * Created by Abhishek Thanvi on 09/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

@Module
public class OTPModule {

    private AppCompatActivity appCompatActivity;

    public OTPModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @OTPScope
    public OTPVerificationActivityView view(){
        return new OTPVerificationActivityView(appCompatActivity);
    }

    @Provides
    @OTPScope
    public OTPModel otpModel(APIInterface apiInterface){
        return new OTPModel(appCompatActivity,apiInterface);
    }

    @Provides
    @OTPScope
    public OTPPresenter otpPresenter(OTPVerificationActivityView view, OTPModel model, PreferenceManger preferenceManger, ABDatabase abDatabase){
        return new OTPPresenter(view,model,preferenceManger, abDatabase);
    }
}
