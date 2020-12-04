package in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login;


import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register.RegisterScope;
import in.appnow.astrobuddy.user_authentication_module.fragments.ForgotPasswordFragment;
import in.appnow.astrobuddy.user_authentication_module.fragments.ForgotPasswordTwoFragment;
import in.appnow.astrobuddy.user_authentication_module.fragments.LoginFragment;

/**
 * Created by Abhishek Thanvi on 12/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

@RegisterScope
@Component(modules = {LoginModule.class},dependencies = AppComponent.class)
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
    void inject(LoginFragment loginFragment);
    void inject(ForgotPasswordFragment fragment);
    void inject(ForgotPasswordTwoFragment fragment);
}
