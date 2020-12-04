package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment;

import android.os.Bundle;
import android.view.View;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.jobs.PostUserStatsDailyJob;
import in.appnow.astrobuddy.jobs.TipOfTheDailyJob;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.AppDownloadRequest;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.rest.request.LoginRequestModel;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import in.appnow.astrobuddy.utils.DeviceUtils;
import in.appnow.astrobuddy.utils.TextUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static in.appnow.astrobuddy.app.AstroAppConstants.DOB;
import static in.appnow.astrobuddy.app.AstroAppConstants.EMAIL;
import static in.appnow.astrobuddy.app.AstroAppConstants.F_NAME;
import static in.appnow.astrobuddy.app.AstroAppConstants.GENDER;
import static in.appnow.astrobuddy.app.AstroAppConstants.L_NAME;
import static in.appnow.astrobuddy.app.AstroAppConstants.PHOTO;
import static in.appnow.astrobuddy.app.AstroAppConstants.SOCIAL_ID;
import static in.appnow.astrobuddy.app.AstroAppConstants.SOCIAL_TYPE_ID;

/**
 * Created by Abhishek Thanvi on 16/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class LoginFragmentPresenter {


    private static final String TAG = LoginFragmentPresenter.class.getSimpleName();
    private final LoginFragmentView loginFragmentView;
    private final LoginFragmentModel loginFragmentModel;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PreferenceManger preferenceManger;

    public LoginFragmentPresenter(LoginFragmentView loginFragmentView, LoginFragmentModel loginFragmentModel, PreferenceManger preferenceManger) {
        this.preferenceManger = preferenceManger;
        this.loginFragmentView = loginFragmentView;
        this.loginFragmentModel = loginFragmentModel;
    }


    public void onCreate() {
        disposable.add(observeGoogleButton());
        disposable.add(observeFacebookButton());
        disposable.add(observeTwitterButton());
        disposable.add(observeLoginButton());
        disposable.add(observeRegisterButton());
        disposable.add(observeForgotPasswordButton());
        disposable.add(observeMobileNumber());
    }

    private Disposable observeGoogleButton() {
        return loginFragmentView.observeGoogleButton().subscribe(data -> loginFragmentView.signInGoogle());
    }

    private Disposable observeMobileNumber() {
        return loginFragmentView.mobileNumberTextWatcher()
                .subscribe(text -> {
                    if (text.toString().trim().length() == 0) {
                        loginFragmentView.changeCountryCodeVisibility(View.VISIBLE);
                    } else if (text.toString().trim().length() == 1) {
                        if (TextUtils.isNumberExist(text.toString().trim()))
                            loginFragmentView.changeCountryCodeVisibility(View.VISIBLE);
                        else loginFragmentView.changeCountryCodeVisibility(View.GONE);
                    }
                });
    }

    private Disposable observeFacebookButton() {
        return loginFragmentView.observeFacebookButton().subscribe(data -> loginFragmentView.signInFb());
    }


    private Disposable observeTwitterButton() {
        return loginFragmentView.observeTwitterButton().subscribe(data -> loginFragmentView.signInTwitter());
    }


    private Disposable observeRegisterButton() {
        return loginFragmentView.registerNowButton().subscribe(data -> loginFragmentView.goToRegistration(null));
    }

    private Disposable observeForgotPasswordButton() {
        return loginFragmentView.forgotPasswordButton().subscribe(data -> loginFragmentView.goToForgotPasswordFragment());
    }

    private Disposable observeLoginButton() {
        return loginFragmentView.webLoginButton()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(loginFragmentView.appCompatActivity.getSupportFragmentManager()))
                .map(isConnected -> loginFragmentView.validateLogin() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        LoginRequestModel loginRequestModel = new LoginRequestModel();
                        loginRequestModel.setId(loginFragmentView.mobile_number.getText().toString());
                        loginRequestModel.setPassword(loginFragmentView.password_edittext.getText().toString());
                        loginRequestModel.setDeviceType("1");
                        loginRequestModel.setLoginAction(AstroAppConstants.LOGIN_EMAIL_MOBILE);
                        loginRequestModel.setFcmToken(loginFragmentView.preferenceManger.getStringValue(PreferenceManger.FCM_TOKEN));
                        loginRequestModel.setCountryCode(loginFragmentView.getCountryCode());
                        //set Device Data
                        AppDownloadRequest request = DeviceUtils.getDeviceDataForAppDownload(loginFragmentView.appCompatActivity);
                        if (request != null) {
                            loginRequestModel.setSerial(request.getSerial());
                            loginRequestModel.setImei1(request.getImei1());
                            loginRequestModel.setImei2(request.getImei2());
                            loginRequestModel.setImeiDeviceId(request.getImeiDeviceId());
                            loginRequestModel.setDeviceId(request.getAndroidUniqueId());
                        }
                        return loginFragmentModel.loginUser(loginRequestModel);
                    } else {
                        return Observable.just(new LoginResponseModel());
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(loginFragmentView.appCompatActivity.getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<LoginResponseModel>(loginFragmentView) {
                    @Override
                    protected void onSuccess(LoginResponseModel data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (!data.isLoggedIn()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else {
                                loginSuccess(data);
                            }
                        }
                    }
                });
    }


    private void loginSuccess(LoginResponseModel loginResponseModel) {
        //schedule daily job
        TipOfTheDailyJob.schedule(loginFragmentView.getContext());
        PostUserStatsDailyJob.schedule(loginFragmentView.getContext());
        //update daily job preference
        preferenceManger.putBoolean(PreferenceManger.TOD_DAILY_JOB, true);
        preferenceManger.putBoolean(PreferenceManger.USER_STATS_DAILY_JOB, true);

        preferenceManger.putUserDetails(loginResponseModel);
        preferenceManger.putString(PreferenceManger.AUTH_TOKEN, loginResponseModel.getUserProfile().getUserToken());
        preferenceManger.putString(PreferenceManger.NODE_AUTH_TOKEN, loginResponseModel.getUserProfile().getNodeToken());
        ToastUtils.shortToast("Login success.");
        HomeActivity.startMainActivity(loginFragmentView.appCompatActivity);
    }

    public void loginUserWithSocial(String id, String f_name, String l_name, String dob, String email, String gender, String photo, String socialType) {
        ProgressDialogFragment.showProgress(loginFragmentView.appCompatActivity.getSupportFragmentManager());
        disposable.add(
                loginFragmentModel.loginUser(loginFragmentView.loginWithSocial(id, socialType))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(__ -> ProgressDialogFragment.dismissProgress(loginFragmentView.appCompatActivity.getSupportFragmentManager()))
                        .subscribeWith(new CallbackWrapper<LoginResponseModel>(loginFragmentView) {
                            @Override
                            protected void onSuccess(LoginResponseModel loginResponseModel) {
                                if (loginResponseModel.isLoggedIn()) {
                                    loginSuccess(loginResponseModel);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(SOCIAL_ID, id);
                                    bundle.putString(F_NAME, f_name);
                                    bundle.putString(L_NAME, l_name);
                                    bundle.putString(DOB, dob);
                                    bundle.putString(SOCIAL_TYPE_ID, socialType);
                                    bundle.putString(GENDER, gender);
                                    bundle.putString(PHOTO, photo);
                                    bundle.putString(EMAIL, email);
                                    loginFragmentView.goToRegistration(bundle);
                                }
                            }
                        }));
    }

    public void onDestroy() {
        disposable.clear();
    }


}
