package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.ResetPasswordRequest;
import in.appnow.astrobuddy.rest.response.ResetPasswordResponse;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by NILESH BHARODIYA on 10-09-2019.
 */
public class ForgotPasswordTwoPresenter implements BasePresenter {

    private final ForgotPasswordTwoView view;
    private final ForgotPasswordTwoModel model;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private String number;
    private String countryCode;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public ForgotPasswordTwoPresenter(ForgotPasswordTwoView view, ForgotPasswordTwoModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observeSendPasswordButton());

    }

    public void fetchData(String number, String countryCode) {
        this.number = number;
        this.countryCode = countryCode;
    }

    private Disposable observeSendPasswordButton() {
        return view.observeSubmitPasswordButton()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(view.appCompatActivity.getSupportFragmentManager()))
                .map(isConnected -> view.isValidated() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        ResetPasswordRequest request = new ResetPasswordRequest();
                        request.setMobileNumber(number);
                        request.setCountryCode(countryCode);
                        request.setNewPassword(view.fetchPasswordFromView());
                        request.setOtp(view.fetchOTPFromView());

                        return model.resetPassword(request);
                    } else {
                        return Observable.just(new ResetPasswordResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(view.appCompatActivity.getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<ResetPasswordResponse>(view, this) {
                    @Override
                    protected void onSuccess(ResetPasswordResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                if (!data.isOtpMatch() || !data.isPasswordUpdated()) {
                                    ToastUtils.shortToast(data.getErrorMessage());

                                } else {
                                    ToastUtils.shortToast(data.getErrorMessage());
                                    model.onPasswordReset();
                                }

                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());
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
