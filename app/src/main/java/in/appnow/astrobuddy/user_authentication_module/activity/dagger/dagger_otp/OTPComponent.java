package in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_otp;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.OTPVerificationActivity;

/**
 * Created by Abhishek Thanvi on 09/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

@OTPScope
@Component(modules = {OTPModule.class},dependencies = AppComponent.class)
public interface OTPComponent {

    void inject(OTPVerificationActivity otpVerificationActivity);

}
