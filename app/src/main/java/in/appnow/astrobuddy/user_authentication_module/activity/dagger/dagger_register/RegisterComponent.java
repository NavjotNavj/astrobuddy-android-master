package in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.RegistrationActivity;
import in.appnow.astrobuddy.user_authentication_module.fragments.RegisterFragmentStepOne;
import in.appnow.astrobuddy.user_authentication_module.fragments.RegisterFragmentStepTwo;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

@RegisterScope
@Component(modules = {RegisterModule.class},dependencies = AppComponent.class)

public interface RegisterComponent {
    void inject(RegistrationActivity registrationActivity);
       void inject(RegisterFragmentStepOne fragment);
       void inject(RegisterFragmentStepTwo fragment);
}
