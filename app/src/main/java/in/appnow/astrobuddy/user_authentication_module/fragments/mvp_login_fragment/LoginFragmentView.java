package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.rilixtech.CountryCodePicker;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.request.AppDownloadRequest;
import in.appnow.astrobuddy.rest.request.LoginRequestModel;
import in.appnow.astrobuddy.user_authentication_module.activity.RegistrationActivity;
import in.appnow.astrobuddy.user_authentication_module.fragments.ForgotPasswordFragment;
import in.appnow.astrobuddy.utils.DeviceUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.TextUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 16/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class LoginFragmentView extends BaseViewClass implements BaseView {

    private static final String TAG = LoginFragmentView.class.getSimpleName();
    AppCompatActivity appCompatActivity;
    PreferenceManger preferenceManger;
    @BindView(R.id.mobile_number)
    AppCompatEditText mobile_number;
    @BindView(R.id.password_edittext)
    AppCompatEditText password_edittext;
    @BindView(R.id.login_button)
    Button login_button;
    @BindView(R.id.forgot_password_button)
    TextView forgot_password_button;
    @BindView(R.id.fb_login_button)
    ImageButton fb_login_button;
    @BindView(R.id.google_login_button)
    ImageButton google_login_button;
    @BindView(R.id.twitter_login_button)
    ImageButton twitter_login_button;
    @BindView(R.id.register_now_button)
    TextView register_now_button;
    @BindView(R.id.login_launcher_logo)
    ImageView loginLogo;
    @BindView(R.id.login_ccp)
    CountryCodePicker countryCodePicker;

    @BindString(R.string.unknown_error)
    String unknownErrorString;
    //Social Login Process
    private GoogleSignInClient mGoogleSignInClient;
    public static int GOOGLE_LOGIN_REQUEST = 100;
    public CallbackManager callbackManager;
    public TwitterAuthClient client;
    private TwitterSession twitterSession;

    public LoginFragmentView(@NonNull AppCompatActivity appCompatActivity, PreferenceManger preferenceManger) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.preferenceManger = preferenceManger;
        inflate(getContext(), R.layout.login_fragment, this);
        ButterKnife.bind(this);
        ImageUtils.setDrawableImage(appCompatActivity, loginLogo, R.drawable.ic_logo_with_name);
        initGoogleLogin();
        initFacebook();
        initTwitter();

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
                TextUtils.setTextMaxLength(mobile_number, 10);
            } else {
                TextUtils.setTextMaxLength(mobile_number, 15);
            }
        }
    }

    public void initFacebook() {
        callbackManager = CallbackManager.Factory.create();
    }

    public void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(appCompatActivity, gso);
    }

    public void initTwitter() {

        client = new TwitterAuthClient();
    }

    public void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        appCompatActivity.startActivityForResult(signInIntent, GOOGLE_LOGIN_REQUEST);
    }

    public void signInFb() {
        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");
        LoginManager.getInstance().logInWithReadPermissions(appCompatActivity, permissionNeeds);
    }

    public void signInTwitter() {
        client.authorize(appCompatActivity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
            }

            @Override
            public void failure(TwitterException e) {
                Logger.DebugLog("Logged Failed Twitter", e.getLocalizedMessage());
            }
        });
    }

    public TwitterSession twitterSession() {
        return twitterSession;
    }

    public boolean validateLogin() {
        if (mobile_number.getText().toString().isEmpty()) {
            ToastUtils.shortToast("Enter Mobile Number or Email");
            return false;
        } else if (password_edittext.getText().toString().isEmpty()) {
            ToastUtils.shortToast("Enter PIN");
            return false;
        } else if (password_edittext.getText().toString().length() != 4) {
            ToastUtils.shortToast("PIN length should be 4");
            return false;
        }
        return true;
    }


    public LoginRequestModel loginWithSocial(String id, String socialType) {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setId(id);
        loginRequestModel.setPassword(socialType);
        loginRequestModel.setDeviceType("1");
        loginRequestModel.setLoginAction(AstroAppConstants.LOGIN_SOCIAL);
        loginRequestModel.setFcmToken(preferenceManger.getStringValue(PreferenceManger.FCM_TOKEN));
        //set Device Data
        AppDownloadRequest request = DeviceUtils.getDeviceDataForAppDownload(appCompatActivity);
        if (request != null) {
            loginRequestModel.setSerial(request.getSerial());
            loginRequestModel.setImei1(request.getImei1());
            loginRequestModel.setImei2(request.getImei2());
            loginRequestModel.setImeiDeviceId(request.getImeiDeviceId());
            loginRequestModel.setDeviceId(request.getAndroidUniqueId());
        }
        return loginRequestModel;
    }


    public void goToRegistration(Bundle bundle) {
        if (bundle != null) {
            appCompatActivity.startActivity(new Intent(appCompatActivity, RegistrationActivity.class).putExtras(bundle));
        } else {
            appCompatActivity.startActivity(new Intent(appCompatActivity, RegistrationActivity.class));
        }

    }

    public void goToForgotPasswordFragment() {
        FragmentUtils.replaceFragmentCommon(appCompatActivity.getSupportFragmentManager(), R.id.frame, ForgotPasswordFragment.newInstance(), FragmentUtils.FORGOT_PASSWORD_FRAGMENT, true);
    }

    public Observable<Object> observeGoogleButton() {
        return RxView.clicks(google_login_button);
    }

    public Observable<Object> observeFacebookButton() {
        return RxView.clicks(fb_login_button);
    }

    public Observable<Object> observeTwitterButton() {
        return RxView.clicks(twitter_login_button);
    }

    public Observable<Object> webLoginButton() {
        return RxView.clicks(login_button);
    }

    public Observable<Object> forgotPasswordButton() {
        return RxView.clicks(forgot_password_button);
    }

    public Observable<CharSequence> mobileNumberTextWatcher() {
        return RxTextView.textChanges(mobile_number);
    }

    public Observable<Object> registerNowButton() {
        return RxView.clicks(register_now_button);
    }

    public void changeCountryCodeVisibility(int visibility) {
        countryCodePicker.setVisibility(visibility);
        if (visibility == View.GONE) {
            TextUtils.setTextMaxLength(mobile_number, 40);
        } else {
            onCountryCodeSelected(countryCodePicker.getSelectedCountryCode());
        }
    }

    public String getCountryCode() {
        if (countryCodePicker != null && !android.text.TextUtils.isEmpty(countryCodePicker.getSelectedCountryCode())) {
            return countryCodePicker.getSelectedCountryCode();
        }
        return "";
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


