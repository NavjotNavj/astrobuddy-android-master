package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.rilixtech.CountryCodePicker;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.request.ResetPasswordRequest;
import in.appnow.astrobuddy.user_authentication_module.fragments.ForgotPasswordTwoFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.TextUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 14/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


@SuppressLint("ViewConstructor")
public class ForgotPasswordView extends BaseViewClass implements BaseView {

    private static final String TAG = ForgotPasswordView.class.getSimpleName();
    @BindView(R.id.forgot_password_mobile_number)
    AppCompatEditText forgot_password_mobile_number;
    @BindView(R.id.send_password_button)
    Button send_password_button;
    @BindView(R.id.forgot_password_logo)
    ImageView forgotPasswordLogo;
    @BindView(R.id.forgot_ccp)
    CountryCodePicker countryCodePicker;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    AppCompatActivity appCompatActivity;

    public ForgotPasswordView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        inflate(getContext(), R.layout.forgot_password_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setDrawableImage(appCompatActivity, forgotPasswordLogo, R.drawable.ic_logo_with_name);
        if (countryCodePicker != null && !android.text.TextUtils.isEmpty(countryCodePicker.getSelectedCountryCode())) {
            onCountryCodeSelected(countryCodePicker.getSelectedCountryCode());
            countryCodePicker.setOnCountryChangeListener(selectedCountry -> {
                onCountryCodeSelected(selectedCountry.getPhoneCode());
            });
        }

    }

    private void onCountryCodeSelected(String countryCode) {
        if (!android.text.TextUtils.isEmpty(countryCode)) {
            if (countryCode.equalsIgnoreCase("91")) {
                TextUtils.setTextMaxLength(forgot_password_mobile_number, 10);
            } else {
                TextUtils.setTextMaxLength(forgot_password_mobile_number, 15);
            }
        }
    }

    public boolean isValidated() {
        if (forgot_password_mobile_number.getText().toString().isEmpty()) {
            ToastUtils.shortToast("Enter Mobile Number");
            return false;
        } else {
            return true;
        }
    }

    public Observable<Object> observeSendPasswordButton() {
        return RxView.clicks(send_password_button);
    }

    public String getCountryCode() {
        return countryCodePicker.getSelectedCountryCode();
    }

    public void goToForgotPasswordTwoFragment(String number, String countryCode) {
        FragmentUtils.replaceFragmentCommon(appCompatActivity.getSupportFragmentManager(),
                R.id.frame,
                ForgotPasswordTwoFragment.getInstance(number, countryCode),
                FragmentUtils.FORGOT_PASSWORD_TWO_FRAGMENT,
                true);
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
