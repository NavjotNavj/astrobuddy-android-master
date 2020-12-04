package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.RegisterMobileRequest;
import in.appnow.astrobuddy.rest.response.OTPResponseModel;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterPresenter {

    private static final String TAG = RegisterPresenter.class.getSimpleName();
    private final RegisterActivityView view;

    public final RegisterModel registerModel;

    private final CompositeDisposable disposable = new CompositeDisposable();


    public RegisterPresenter(RegisterActivityView view, RegisterModel registerModel) {
        this.view = view;
        this.registerModel = registerModel;
    }

    public void onCreate() {
        disposable.add(observeNextButton());
        disposable.add(observeCancelButton());
    }

    private Disposable observeNextButton() {
        return view.observeNextButton()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(view.appCompatActivity.getSupportFragmentManager()))
                .map(isConnected -> view.openFragmentStepTwo() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        RegisterMobileRequest registerMobileRequest = new RegisterMobileRequest();
                        registerMobileRequest.setMobileNumber(view.submitData().getMobile());
                        registerMobileRequest.setCountryCode(view.submitData().getCountryCode());
                        registerMobileRequest.setEmailId(view.submitData().getEmail());
                        registerMobileRequest.setFirstName(view.submitData().getFname());
                        registerMobileRequest.setLastName(view.submitData().getLname());
                        return registerModel.registerOTP(registerMobileRequest);
                    } else {
                        return Observable.just(new OTPResponseModel());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())

                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(view.appCompatActivity.getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<OTPResponseModel>(view) {
                    @Override
                    protected void onSuccess(OTPResponseModel data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (data.isUserMatch()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (data.isOtpDbInsert()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                                view.openOtpActivity();
                            }
                        }
                    }
                });
    }


    private Disposable observeCancelButton() {
        return view.observeCancelButton().subscribe(data -> view.openFragmentStepOne());
    }


    public void onDestroy() {
        disposable.clear();
    }


}
