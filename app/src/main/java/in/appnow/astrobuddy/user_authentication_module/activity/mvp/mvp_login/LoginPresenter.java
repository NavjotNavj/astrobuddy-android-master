package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_login;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Abhishek Thanvi on 12/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class LoginPresenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();
    private final LoginView loginView;
    private final LoginModel loginModel;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public LoginPresenter(LoginView view, LoginModel loginModel) {
        this.loginView = view;
        this.loginModel = loginModel;
    }

    public void onCreate() {

    }



    public void onDestroy() {
        disposable.clear();
    }

}
