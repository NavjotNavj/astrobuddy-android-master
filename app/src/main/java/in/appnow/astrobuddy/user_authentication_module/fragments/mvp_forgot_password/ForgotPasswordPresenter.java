package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.RegisterMobileRequest;
import in.appnow.astrobuddy.rest.request.ResetPasswordRequest;
import in.appnow.astrobuddy.rest.response.ForgotPasswordResponse;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Abhishek Thanvi on 14/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class ForgotPasswordPresenter implements BasePresenter {


    private static final String TAG = ForgotPasswordPresenter.class.getSimpleName();
    private final ForgotPasswordView forgotPasswordView;

    private final ForgotPasswordModel forgotPasswordModel;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public ForgotPasswordPresenter(ForgotPasswordView forgotPasswordView, ForgotPasswordModel forgotPasswordModel, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.forgotPasswordView = forgotPasswordView;
        this.forgotPasswordModel = forgotPasswordModel;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }


    private Disposable observeSendPasswordButton() {
        return forgotPasswordView.observeSendPasswordButton()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(forgotPasswordView.appCompatActivity.getSupportFragmentManager()))
                .map(isConnected -> forgotPasswordView.isValidated() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        RegisterMobileRequest registerMobileRequest = new RegisterMobileRequest();
                        registerMobileRequest.setMobileNumber(forgotPasswordView.forgot_password_mobile_number.getText().toString());
                        registerMobileRequest.setCountryCode(forgotPasswordView.getCountryCode());
                        return forgotPasswordModel.forgotPassword(registerMobileRequest);
                    } else {
                        return Observable.just(new ForgotPasswordResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(forgotPasswordView.appCompatActivity.getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<ForgotPasswordResponse>(forgotPasswordView, this) {
                    @Override
                    protected void onSuccess(ForgotPasswordResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                forgotPasswordView.goToForgotPasswordTwoFragment(
                                        forgotPasswordView.forgot_password_mobile_number.getText().toString(),
                                        forgotPasswordView.getCountryCode()
                                );
                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());
                            }

                        }
                    }
                });
    }

    @Override
    public void onCreate() {
        disposable.add(observeSendPasswordButton());

    }

    @Override
    public void onDestroy() {
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                forgotPasswordModel.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        forgotPasswordModel.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        forgotPasswordModel.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(forgotPasswordModel.getAppCompatActivity());
                    }
                });
    }
}
