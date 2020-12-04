package in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register.RegisterActivityView;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register.RegisterModel;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register.RegisterPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one.RegisterFragmentOneModel;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one.RegisterFragmentOnePresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one.RegisterFragmentOneView;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_two.RegisterFragmentTwoModel;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_two.RegisterFragmentTwoPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_two.RegisterFragmentTwoView;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */

@Module
public class RegisterModule {

    private AppCompatActivity appCompatActivity;

    public RegisterModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @RegisterScope
    public RegisterActivityView view(PreferenceManger preferenceManger){
        return new RegisterActivityView(appCompatActivity,preferenceManger);
    }

    @Provides
    @RegisterScope
    public RegisterModel registerModel(APIInterface apiInterface){
        return new RegisterModel(appCompatActivity,apiInterface);
    }

    @Provides
    @RegisterScope
    public RegisterPresenter registerPresenter(RegisterActivityView view, RegisterModel model){
        return new RegisterPresenter(view,model);
    }


    // Registration Step One MVP
    @Provides
    @RegisterScope
    public RegisterFragmentOneView registerFragmentOneView(){
        return new RegisterFragmentOneView(appCompatActivity);
    }

    @Provides
    @RegisterScope
    public RegisterFragmentOneModel registerFragmentOneModel(APIInterface apiInterface){
        return new RegisterFragmentOneModel(appCompatActivity);
    }

    @Provides
    @RegisterScope
    public RegisterFragmentOnePresenter registerFragmentOnePresenter(RegisterFragmentOneView view
            , RegisterFragmentOneModel model, PreferenceManger preferenceManger, ABDatabase abDatabase){
        return new RegisterFragmentOnePresenter(view,model, preferenceManger, abDatabase);
    }

    // Registration Step Two MVP
    @Provides
    @RegisterScope
    public RegisterFragmentTwoView registerFragmentTwoView(){
        return new RegisterFragmentTwoView(appCompatActivity);
    }

    @Provides
    @RegisterScope
    public RegisterFragmentTwoModel registerFragmentTwoModel(APIInterface apiInterface){
        return new RegisterFragmentTwoModel(appCompatActivity);
    }

    @Provides
    @RegisterScope
    public RegisterFragmentTwoPresenter registerFragmentTwoPresenter(RegisterFragmentTwoView view
            , RegisterFragmentTwoModel model, PreferenceManger preferenceManger, ABDatabase abDatabase){
        return new RegisterFragmentTwoPresenter(view,model, preferenceManger, abDatabase);
    }


}
