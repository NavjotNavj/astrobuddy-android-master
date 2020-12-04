package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.TabPagerAdapter;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.custom_views.NonSwipeableViewPager;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.request.AppDownloadRequest;
import in.appnow.astrobuddy.rest.request.RegistrationRequestModel;
import in.appnow.astrobuddy.ui.common_activity.CommonActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.OTPVerificationActivity;
import in.appnow.astrobuddy.user_authentication_module.fragments.RegisterFragmentStepOne;
import in.appnow.astrobuddy.user_authentication_module.fragments.RegisterFragmentStepTwo;
import in.appnow.astrobuddy.user_authentication_module.interfaces.OnValidationListener;
import in.appnow.astrobuddy.utils.DeviceUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.LocaleUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import in.appnow.astrobuddy.utils.VectorUtils;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


@SuppressLint("ViewConstructor")
public class RegisterActivityView extends BaseViewClass implements BaseView {


    @BindView(R.id.first_circle)
    ImageView first_circle;
    @BindView(R.id.second_circle)
    ImageView second_circle;
    @BindView(R.id.registration_viewpager)
    NonSwipeableViewPager registration_viewpager;
    @BindView(R.id.cancel_action)
    Button cancel_action;
    @BindView(R.id.next_action)
    Button next_action;
    @BindView(R.id.bottom_panel)
    LinearLayout bottom_panel;
    @BindView(R.id.register_terms_conditions_label)
    TextView termsConditionLabel;

    @BindView(R.id.background_image)
    ImageView backgroundImageView;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    AppCompatActivity appCompatActivity;
    TabPagerAdapter registrationFragmentViewPagerAdapter;
    PreferenceManger preferenceManger;

    public RegisterActivityView(@NonNull AppCompatActivity appCompatActivity, PreferenceManger preferenceManger) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.preferenceManger = preferenceManger;
        inflate(getContext(), R.layout.activity_registration, this);
        ButterKnife.bind(this);
        setPrivacyPolicySpannable();
        ImageUtils.setBackgroundImage(appCompatActivity, backgroundImageView);
    }

    private void setPrivacyPolicySpannable() {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "By Signing Up you agree to the ");
        spanTxt.append(" Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                CommonActivity.openCommonActivity(appCompatActivity, "Privacy Policy", FragmentUtils.WEB_VIEW_FRAGMENT);
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        spanTxt.append(", please review.");
        termsConditionLabel.setMovementMethod(LinkMovementMethod.getInstance());
        termsConditionLabel.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    public void setRegistration_viewpager(Bundle bundle) {
        registrationFragmentViewPagerAdapter = new TabPagerAdapter(appCompatActivity.getSupportFragmentManager());
        registrationFragmentViewPagerAdapter.addFragment(RegisterFragmentStepOne.newInstance(bundle));
        registrationFragmentViewPagerAdapter.addFragment(RegisterFragmentStepTwo.newInstance(bundle));

        registration_viewpager.setAdapter(registrationFragmentViewPagerAdapter);
        stepOneViewManager();
    }

    public Observable<Object> observeNextButton() {
        return RxView.clicks(next_action);
    }

    public Observable<Object> observeCancelButton() {
        return RxView.clicks(cancel_action);
    }

    public boolean openFragmentStepTwo() {
        if (registration_viewpager.getCurrentItem() == 0) {
            if (validateStepsData()) {
                stepTwoViewManager();
            }
            return false;
        } else if (registration_viewpager.getCurrentItem() == 1) {
            if (validateStepsData()) {
                return true;
            }
        }
        return false;
    }


    public void openFragmentStepOne() {
        if (registration_viewpager.getCurrentItem() == 1) {
            stepOneViewManager();
        } else {
            appCompatActivity.finish();
        }
    }

    public void stepOneViewManager() {
        registration_viewpager.setCurrentItem(0);
        cancel_action.setText("Cancel");
        next_action.setText("Next");
        VectorUtils.setVectorCompoundDrawable(next_action, appCompatActivity, 0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0, R.color.white);

        second_circle.setBackground(ImageUtils.changeShapeColor(appCompatActivity, R.drawable.circle_bg, R.color.white));

        first_circle.setImageDrawable(null);
        first_circle.setBackground(ImageUtils.changeShapeColor(appCompatActivity, R.drawable.circle_bg, R.color.colorPrimary));
        cancel_action.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public void stepTwoViewManager() {
        registration_viewpager.setCurrentItem(1);
        cancel_action.setVisibility(VISIBLE);
        cancel_action.setText("Back");
        next_action.setText("Submit");
        next_action.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        first_circle.setImageResource(R.drawable.ic_done_black_24dp);
        ImageUtils.changeImageColor(first_circle, appCompatActivity, R.color.black);

        VectorUtils.setVectorCompoundDrawable(cancel_action, appCompatActivity, R.drawable.ic_keyboard_arrow_left_black_24dp, 0, 0, 0, R.color.white);
        second_circle.setBackground(ImageUtils.changeShapeColor(appCompatActivity, R.drawable.circle_bg, R.color.colorPrimary));

    }

    public boolean validateStepsData() {
        OnValidationListener onValidationListener;
        int currentPosition = getCurrentPage();
        Fragment currentFragment = registrationFragmentViewPagerAdapter.getItem(currentPosition);
        if (currentFragment != null) {
            onValidationListener = (OnValidationListener) currentFragment;
            return onValidationListener.checkValidation();
        }
        return false;
    }

    public RegistrationRequestModel submitData() {
        RegistrationRequestModel registrationRequestModel = new RegistrationRequestModel();
        Fragment registerFragmentOne = registrationFragmentViewPagerAdapter.getItem(0);
        if (registerFragmentOne != null) {
            if (registerFragmentOne instanceof RegisterFragmentStepOne) {
                String firstName = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getFirstName();
                String lastName = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getLastName();
                String dateOfBirth = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getDateOfBirth();
                String timeOfBirth = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getTimeOfBirth();
                String placeOfBirth = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getPlaceOfBirth();
                String latLong = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getLatLong();
                String profilePic = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getProfileImage();
                String socialId = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getSocialID();
                int socialRegStatus = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getSocialRegistrationStatus();
                String socialMediaTypeId = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getSocialMediaTypeId();
                String gender = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getGender();
                String maritalStatus = ((RegisterFragmentStepOne) registerFragmentOne).registerFragmentOneView.getMaritalStatus();

                registrationRequestModel.setFname(firstName);
                registrationRequestModel.setLname(lastName);
                registrationRequestModel.setLocation(placeOfBirth);
                registrationRequestModel.setLatlng(latLong);
                registrationRequestModel.setProfileImg(profilePic);
                registrationRequestModel.setSocialId(socialId);
                registrationRequestModel.setSocialRegistrationStatus(socialRegStatus);
                registrationRequestModel.setSocialMediaTypeId(socialMediaTypeId);
                registrationRequestModel.setGender(gender + "");
                registrationRequestModel.setDeviceType("1");
                registrationRequestModel.setLocale(LocaleUtils.fetchCountryISO(appCompatActivity));
                registrationRequestModel.setFcmToken(preferenceManger.getStringValue(PreferenceManger.FCM_TOKEN));
                registrationRequestModel.setDob(dateOfBirth + " " + timeOfBirth);
                registrationRequestModel.setMaritalStatus(maritalStatus);

                //set Device Data
                AppDownloadRequest request = DeviceUtils.getDeviceDataForAppDownload(appCompatActivity);
                if (request != null) {
                    registrationRequestModel.setSerial(request.getSerial());
                    registrationRequestModel.setImei1(request.getImei1());
                    registrationRequestModel.setImei2(request.getImei2());
                    registrationRequestModel.setImeiDeviceId(request.getImeiDeviceId());
                    registrationRequestModel.setDeviceId(request.getAndroidUniqueId());
                }

            }
        }
        Fragment registerFragmentTwo = registrationFragmentViewPagerAdapter.getItem(1);
        if (registerFragmentTwo != null) {
            if (registerFragmentTwo instanceof RegisterFragmentStepTwo) {
                String email = ((RegisterFragmentStepTwo) registerFragmentTwo).registerFragmentTwoView.getEmail();
                String mobile = ((RegisterFragmentStepTwo) registerFragmentTwo).registerFragmentTwoView.getMobileNumber();
                String password = ((RegisterFragmentStepTwo) registerFragmentTwo).registerFragmentTwoView.getPassword();
                String promoCode = ((RegisterFragmentStepTwo) registerFragmentTwo).registerFragmentTwoView.getPromoCode();
                String countryCode = ((RegisterFragmentStepTwo) registerFragmentTwo).registerFragmentTwoView.getCountryCode();
                int getPromoId = ((RegisterFragmentStepTwo) registerFragmentTwo).registerFragmentTwoView.getPromoId();
                registrationRequestModel.setEmail(email);
                registrationRequestModel.setMobile(mobile);
                registrationRequestModel.setPassword(password);
                registrationRequestModel.setCountryCode(countryCode);
                registrationRequestModel.setPromoId(getPromoId);
                registrationRequestModel.setPromoCode(promoCode);
            }
        }

        return registrationRequestModel;
    }

    public void openOtpActivity() {
        Intent intent = new Intent(appCompatActivity, OTPVerificationActivity.class);
        intent.putExtra("Register_Model", submitData());
        appCompatActivity.startActivity(intent);
    }

    private int getCurrentPage() {
        if (registration_viewpager != null) {
            return registration_viewpager.getCurrentItem();
        }
        return 0;
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
