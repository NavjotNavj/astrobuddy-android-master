package in.appnow.astrobuddy.ui.fragments.changePassword.mvp;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.ChangePasswordRequest;
import in.appnow.astrobuddy.rest.response.ChangePasswordResponse;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 11:00, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChangePasswordPresenter implements BasePresenter {
    private static final String TAG = ChangePasswordPresenter.class.getSimpleName();
    private final ChangePasswordView view;
    private final ChangePasswordModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public ChangePasswordPresenter(ChangePasswordView view, ChangePasswordModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observeChangePassword());
    }

    private Disposable observeChangePassword() {
        return view.observeChangeButton()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .map(validate -> view.validateData() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
                        changePasswordRequest.setOldPassword(view.getOldPassword());
                        changePasswordRequest.setNewPassword(view.getNewPassword());
                        changePasswordRequest.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                        return model.changePassword(changePasswordRequest);
                    } else {
                        return Observable.just(new ChangePasswordResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<ChangePasswordResponse>(view, this) {
                    @Override
                    protected void onSuccess(ChangePasswordResponse data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (!data.isUserExist()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (!data.isPasswordMatched()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (!data.isPasswordUpdated()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());
                                view.passwordChangeSuccessfully();
                            }
                        }
                    }
                });

    }


    @Override
    public void onDestroy() {
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                model.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        model.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        model.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(model.getAppCompatActivity());
                    }
                });
    }
}
