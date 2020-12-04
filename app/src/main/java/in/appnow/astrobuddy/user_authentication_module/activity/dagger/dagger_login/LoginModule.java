package in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register.RegisterScope;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_login.LoginModel;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_login.LoginPresenter;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_login.LoginView;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password.ForgotPasswordModel;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password.ForgotPasswordPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password.ForgotPasswordView;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two.ForgotPasswordTwoModel;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two.ForgotPasswordTwoPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two.ForgotPasswordTwoView;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment.LoginFragmentModel;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment.LoginFragmentPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment.LoginFragmentView;

/**
 * Created by Abhishek Thanvi on 12/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

@Module
public class LoginModule {

    private AppCompatActivity appCompatActivity;

    public LoginModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @RegisterScope
    public LoginView view(PreferenceManger preferenceManger){
        return new LoginView(appCompatActivity,preferenceManger);
    }

    @Provides
    @RegisterScope
    public LoginModel loginModel(APIInterface apiInterface){
        return new LoginModel(appCompatActivity,apiInterface);
    }

    @Provides
    @RegisterScope
    public LoginPresenter loginPresenter(LoginView view, LoginModel model){
        return new LoginPresenter(view,model);
    }


    //Login Fragment MVP
    @Provides
    @RegisterScope
    public LoginFragmentView loginFragmentView(PreferenceManger preferenceManger){
        return new LoginFragmentView(appCompatActivity,preferenceManger);
    }

    @Provides
    @RegisterScope
    public LoginFragmentModel loginFragmentModel(APIInterface apiInterface){
        return new LoginFragmentModel(appCompatActivity,apiInterface);
    }

    @Provides
    @RegisterScope
    public LoginFragmentPresenter loginFragmentPresenter(LoginFragmentView view, LoginFragmentModel model,PreferenceManger preferenceManger){
        return new LoginFragmentPresenter(view,model,preferenceManger);
    }


    //Forgot Password MVP

    @Provides
    @RegisterScope
    public ForgotPasswordView forgotPasswordView(){
        return new ForgotPasswordView(appCompatActivity);
    }

    @Provides
    @RegisterScope
    public ForgotPasswordModel forgotPasswordModel(APIInterface apiInterface){
        return new ForgotPasswordModel(appCompatActivity,apiInterface);
    }

    @Provides
    @RegisterScope
    public ForgotPasswordPresenter forgotPasswordPresenter(ForgotPasswordView view,
                                                                ForgotPasswordModel model,
                                                                PreferenceManger preferenceManger,
                                                                ABDatabase abDatabase){
        return new ForgotPasswordPresenter(view,model, preferenceManger, abDatabase);
    }


    @Provides
    @RegisterScope
    public ForgotPasswordTwoView forgotPasswordTwoView(){
        return new ForgotPasswordTwoView(appCompatActivity);
    }

    @Provides
    @RegisterScope
    public ForgotPasswordTwoModel forgotPasswordTwoModel(APIInterface apiInterface){
        return new ForgotPasswordTwoModel(appCompatActivity,apiInterface);
    }

    @Provides
    @RegisterScope
    public ForgotPasswordTwoPresenter forgotPasswordTwoPresenter(ForgotPasswordTwoView view,
                                                                 ForgotPasswordTwoModel model,
                                                                 PreferenceManger preferenceManger,
                                                                 ABDatabase abDatabase){
        return new ForgotPasswordTwoPresenter(view,model, preferenceManger, abDatabase);
    }
}
