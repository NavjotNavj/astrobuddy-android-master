package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp;

import android.text.TextUtils;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.jobs.PostUserStatsDailyJob;
import in.appnow.astrobuddy.jobs.TipOfTheDailyJob;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import in.appnow.astrobuddy.rest.request.RegisterMobileRequest;
import in.appnow.astrobuddy.rest.response.OTPResponseModel;
import in.appnow.astrobuddy.rest.request.RegistrationRequestModel;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Abhishek Thanvi on 09/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class OTPPresenter implements BasePresenter {

    private static final String TAG = OTPPresenter.class.getSimpleName();
    private final OTPVerificationActivityView view;

    public final OTPModel otpModel;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final PreferenceManger preferenceManger;

    public OTPPresenter(OTPVerificationActivityView view, OTPModel otpModel, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.preferenceManger = preferenceManger;
        this.otpModel = otpModel;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observerConfirmOtpAction());
        disposable.add(observeResendOTP());
        disposable.add(observeFirstOTP());
        disposable.add(observeSecondOTP());
        disposable.add(observeThirdOTP());
        disposable.add(observeFourthOTP());
        if (view != null) {
            String countryCode = otpModel.getRegisterRequestData().getCountryCode();
            if (TextUtils.isEmpty(countryCode)) {
                countryCode = "" ;
            }
            view.updateOtpMessageLabel("+" + countryCode + "-" + otpModel.getRegisterRequestData().getMobile());
            view.startTimer(30);
        }
    }


    private Disposable observeFirstOTP() {
        return view.firstOTPTextWatcher()
                .subscribe(text -> {
                    if (text.toString().trim().length() == 1) {
                        view.changeFocus(1);
                    }
                });
    }

    private Disposable observeSecondOTP() {
        return view.secondOTPTextWatcher()
                .subscribe(text -> {
                    if (text.toString().trim().length() == 1) {
                        view.changeFocus(2);
                    }
                });
    }

    private Disposable observeThirdOTP() {
        return view.thirdOTPTextWatcher()
                .subscribe(text -> {
                    if (text.toString().trim().length() == 1) {
                        view.changeFocus(3);
                    }
                });
    }

    private Disposable observeFourthOTP() {
        return view.fourthOTPTextWatcher()
                .subscribe(text -> {
                    if (text.toString().trim().length() == 1) {
                        //  observerConfirmOtpAction();
                    }
                });
    }

    private Disposable observeResendOTP() {
        return view.resendOtpButtonAction()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(view.getAppCompatActivity().getSupportFragmentManager()))
                .map(isConnected -> AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        RegisterMobileRequest registerMobileRequest = new RegisterMobileRequest();
                        registerMobileRequest.setMobileNumber(otpModel.getRegisterRequestData().getMobile());
                        registerMobileRequest.setCountryCode(otpModel.getRegisterRequestData().getCountryCode());
                        registerMobileRequest.setEmailId(otpModel.getRegisterRequestData().getEmail());
                        registerMobileRequest.setFirstName(otpModel.getRegisterRequestData().getFname());
                        registerMobileRequest.setLastName(otpModel.getRegisterRequestData().getLname());
                        return otpModel.registerOTP(registerMobileRequest);
                    } else return Observable.just(new OTPResponseModel());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(view.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<OTPResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(OTPResponseModel data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (data.isUserMatch()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (data.isOtpDbInsert()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                                view.startTimer(30);
                            }
                        }
                    }
                });
    }


    private Disposable observerConfirmOtpAction() {
        return view.confirmOtpButtonAction()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(view.getAppCompatActivity().getSupportFragmentManager()))
                .map(isConnected -> view.validateSubmitOTP() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        otpModel.getRegisterRequestData().setOtp(view.otp_value_one.getText().toString() + view.otp_value_two.getText().toString()
                                + view.otp_value_three.getText().toString() + view.otp_value_four.getText().toString());
                        return otpModel.registerUser(otpModel.getRegisterRequestData());

                    } else {
                        return Observable.just(new LoginResponseModel());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())

                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(view.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<LoginResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(LoginResponseModel data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (data.isOtpMatched() && data.isUserCreated()) {
                                registrationSuccess(data);
                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());
                            }
                        }
                    }
                });
    }


    public void registerUser(RegistrationRequestModel registrationRequestModel) {
        ProgressDialogFragment.showProgress(view.getAppCompatActivity().getSupportFragmentManager());
        disposable.add(otpModel.registerUser(registrationRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(view.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<LoginResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(LoginResponseModel loginResponseModel) {
                        if (loginResponseModel.isOtpMatched() && loginResponseModel.isUserCreated()) {
                            registrationSuccess(loginResponseModel);
                        } else {
                            ToastUtils.shortToast(loginResponseModel.getErrorMessage());
                        }
                    }
                }));
    }

    private void registrationSuccess(LoginResponseModel loginResponseModel) {
        //schedule daily job
        TipOfTheDailyJob.schedule(view.getContext());
        PostUserStatsDailyJob.schedule(view.getContext());
        //update daily job preference
        preferenceManger.putBoolean(PreferenceManger.TOD_DAILY_JOB, true);
        preferenceManger.putBoolean(PreferenceManger.USER_STATS_DAILY_JOB, true);

        preferenceManger.putUserDetails(loginResponseModel);
        preferenceManger.putString(PreferenceManger.AUTH_TOKEN, loginResponseModel.getUserProfile().getUserToken());
        preferenceManger.putString(PreferenceManger.NODE_AUTH_TOKEN, loginResponseModel.getUserProfile().getNodeToken());
        HomeActivity.startMainActivity(view.getAppCompatActivity());
        ToastUtils.shortToast("Registration success.");
    }

    @Override
    public void onDestroy() {
        view.cancelTimer();
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                otpModel.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        otpModel.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        otpModel.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(otpModel.getAppCompatActivity());
                    }
                });
    }
}
