package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.rilixtech.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.MethodConstants;
import in.appnow.astrobuddy.utils.TextUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterFragmentTwoView extends BaseViewClass {


    private static final String TAG = RegisterFragmentTwoView.class.getSimpleName();
    @BindView(R.id.mobile_edittext)
    AppCompatEditText mobile_edittext;
    @BindView(R.id.email_edittext)
    public AppCompatEditText email_edittext;
    @BindView(R.id.password_edittext)
    AppCompatEditText password_edittext;
    @BindView(R.id.confirm_password_edittext)
    AppCompatEditText confirm_password_edittext;
    @BindView(R.id.have_promo_code)
    AppCompatTextView have_promo_code;
    @BindView(R.id.applied_promo_code_label)
    TextView applied_code;
    @BindView(R.id.promo_code_applied_message_label)
    TextView promo_applied_message;
    @BindView(R.id.remove_promo_code)
    ImageView removePromoCode;
    @BindView(R.id.promo_code_applied_layout)
    LinearLayout promoCodeLayout;
    @BindView(R.id.register_ccp)
    CountryCodePicker countryCodePicker;

    private String mobileNumber = "";
    private String email = "";
    private String password = "";
    private String promoCode = "";
    private int promoId = 1;//1 for no promo applied


    public RegisterFragmentTwoView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        inflate(getContext(), R.layout.registration_second_fragment, this);
        ButterKnife.bind(this, this);
        if (countryCodePicker != null && !android.text.TextUtils.isEmpty(countryCodePicker.getSelectedCountryCode())) {
            onCountryCodeSelected(countryCodePicker.getSelectedCountryCode());
            countryCodePicker.setOnCountryChangeListener(selectedCountry -> {
                onCountryCodeSelected(selectedCountry.getPhoneCode());
            });
        }
        promo_applied_message.setTextColor(appCompatActivity.getResources().getColor(R.color.white));
        ImageUtils.changeImageColor(removePromoCode, appCompatActivity, R.color.white);
    }

    private void onCountryCodeSelected(String countryCode) {
        if (!android.text.TextUtils.isEmpty(countryCode)) {
            if (countryCode.equalsIgnoreCase("91")) {
                TextUtils.setTextMaxLength(mobile_edittext, 10);
            } else {
                TextUtils.setTextMaxLength(mobile_edittext, 15);
            }
        }
    }

    public Observable<Object> observePromoCodeButton() {
        return RxView.clicks(have_promo_code);
    }

    public Observable<Object> observeRemovePromoCode() {
        return RxView.clicks(removePromoCode);
    }


    public void onPromoCodeApplied(boolean isApplied) {
        if (isApplied) {
            have_promo_code.setVisibility(GONE);
            promoCodeLayout.setVisibility(VISIBLE);
        } else {
            have_promo_code.setVisibility(VISIBLE);
            promoCodeLayout.setVisibility(GONE);
        }
    }

    public void updatePromoCodeViews(ApplyPromoCodeResponse response, String promoCode, int promoId) {
        this.promoId = promoId;
        if (response == null) {
            promo_applied_message.setText("");
            applied_code.setText("");
        } else {
            promo_applied_message.setText(response.getPromoLabel());
            applied_code.setText(promoCode);
        }
    }

    public boolean validateData() {
        if (mobile_edittext.getText().toString().trim().isEmpty()) {
            ToastUtils.shortToast("Enter Mobile Number");
        } else if (email_edittext.getText().toString().trim().isEmpty()) {
            ToastUtils.shortToast("Enter Email");
        } else if (!MethodConstants.emailValidator(email_edittext.getText().toString().trim())) {
            ToastUtils.shortToast("Invalid Email");
        } else if (password_edittext.getText().toString().trim().isEmpty()) {
            ToastUtils.shortToast("Enter PIN");
        } else if (password_edittext.getText().toString().trim().length() != 4) {
            ToastUtils.shortToast("PIN length should be 4");
        } else if (confirm_password_edittext.getText().toString().trim().isEmpty()) {
            ToastUtils.shortToast("Confirm Your PIN");
        } else if (!password_edittext.getText().toString().trim().equals(confirm_password_edittext.getText().toString().trim())) {
            ToastUtils.shortToast("PINs did not Match");
        } else {
            mobileNumber = mobile_edittext.getText().toString().trim();
            email = email_edittext.getText().toString().trim();
            password = password_edittext.getText().toString().trim();
            promoCode = applied_code.getText().toString().trim();
            return true;
        }
        return false;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public int getPromoId() {
        return promoId;
    }

    public String getCountryCode() {
        return countryCodePicker.getSelectedCountryCode();
    }

    public void showDialogToRemovePromoCode(String message) {
        DialogHelperClass.showMessageOKCancel(getContext(), message, "OK", "CANCEL", (dialogInterface, i) -> {
            updatePromoCodeViews(null, "", 1);
            onPromoCodeApplied(false);
        }, (dialogInterface, i) -> {

        });
    }
}
