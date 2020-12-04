package in.appnow.astrobuddy.user_authentication_module.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register.DaggerRegisterComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register.RegisterComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register.RegisterModule;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register.RegisterActivityView;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register.RegisterPresenter;

/**
 * Created by Abhishek Thanvi on 09/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegistrationActivity extends BaseActivity {

    @Inject
    RegisterActivityView registerActivityView;
    @Inject
    RegisterPresenter registerPresenter;

  //  private RxPermissions rxPermissions;
    //private Disposable permissionDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerRegisterComponent.builder().appComponent(AstroApplication.get(this).component())
                .registerModule(new RegisterModule(this))
                .build().inject(this);
        getRegisterComponent();
        setContentView(registerActivityView);
       // rxPermissions = new RxPermissions(this);
        registerPresenter.onCreate();
        registerActivityView.setRegistration_viewpager(registerPresenter.registerModel.getPreData());
       // checkSMSPermission();
    }

    public RegisterComponent getRegisterComponent() {
        return DaggerRegisterComponent
                .builder()
                .appComponent(AstroApplication.get(this).component())
                .registerModule(new RegisterModule(this))
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (permissionDisposable != null) {
            permissionDisposable.dispose();
        }*/
        registerPresenter.onDestroy();

    }

   /* public void checkSMSPermission() {
        permissionDisposable = rxPermissions.request(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                });

    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
