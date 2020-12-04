package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;


/**
 * Created by NILESH BHARODIYA on 10-09-2019.
 */
public class ForgotPasswordTwoView extends BaseViewClass implements BaseView {


    @BindView(R.id.enter_otp)
    EditText otpText;

    @BindView(R.id.enter_new_password)
    EditText newPasswordText;

    @BindView(R.id.enter_confirm_new_password)
    EditText confirmPasswordText;

    @BindView(R.id.password_submit_button)
    Button submitButton;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    AppCompatActivity appCompatActivity;

    public ForgotPasswordTwoView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;

        inflate(getContext(), R.layout.forgot_password_two_frgament, this);
        ButterKnife.bind(this, this);

    }

    public String fetchOTPFromView() {
        return otpText.getText().toString();
    }
    public String fetchPasswordFromView() {
        return newPasswordText.getText().toString();
    }
    public String fetchConfirmPasswordFromView() {
        return confirmPasswordText.getText().toString();
    }

    public Observable<Object> observeSubmitPasswordButton() {
        return RxView.clicks(submitButton);
    }

    public boolean isValidated() {
        String otp = fetchOTPFromView();
        String password = fetchPasswordFromView();
        String newPassword = fetchConfirmPasswordFromView();

        if (TextUtils.isEmpty(otp) || otp.length() != 4) {
            ToastUtils.shortToast("Enter OTP");
            return false;

        } else if (TextUtils.isEmpty(password) || password.length() != 4) {
            ToastUtils.shortToast("Enter Valid Password");
            return false;

        } else if (TextUtils.isEmpty(newPassword) ||
                newPassword.length() != 4 ||
                !password.equalsIgnoreCase(newPassword)) {
            ToastUtils.shortToast("Password and new password are not same!");
            return false;

        } else {
            return true;
        }
    }

    @Override
    public void onUnknownError(String error) {
        ToastUtils.shortToast(error);
    }

    @Override
    public void onTimeout() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        ToastUtils.shortToast(unknownErrorString);
    }

}
