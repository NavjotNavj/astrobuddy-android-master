package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.ResetPasswordRequest;
import in.appnow.astrobuddy.rest.response.ResetPasswordResponse;
import io.reactivex.Observable;

/**
 * Created by NILESH BHARODIYA on 10-09-2019.
 */
public class ForgotPasswordTwoModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public ForgotPasswordTwoModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<ResetPasswordResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return apiInterface.resetPassword(resetPasswordRequest);
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }


    public void onPasswordReset() {
        appCompatActivity.onBackPressed();
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }
}
