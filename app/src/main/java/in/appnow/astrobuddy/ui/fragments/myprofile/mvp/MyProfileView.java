package in.appnow.astrobuddy.ui.fragments.myprofile.mvp;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.helper.glide.GlideApp;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.MethodConstants;
import in.appnow.astrobuddy.utils.StringUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import in.appnow.astrobuddy.utils.VectorUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 16:17, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MyProfileView extends BaseViewClass implements BaseView {

    private static final String TAG = MyProfileView.class.getSimpleName();
    @BindView(R.id.edit_profile_pic_button)
    ImageView editProfilePicButton;
    @BindView(R.id.profile_image_view)
    ImageView profileImageView;
    @BindView(R.id.profile_user_sun_label)
    TextView sunLabel;
    @BindView(R.id.profile_user_dob_label)
    TextView dobLabel;
    @BindView(R.id.profile_user_time_of_birth_label)
    TextView timeOfBirthLabel;
    @BindView(R.id.profile_user_place_of_birth_label)
    TextView placeOfBirthLabel;
    @BindView(R.id.profile_user_mobile_label)
    TextView mobileLabel;
    @BindView(R.id.profile_user_email_label)
    AppCompatEditText emailLabel;
    @BindView(R.id.profile_user_name_label)
    TextView userName;
    @BindView(R.id.profile_user_age_label)
    TextView ageLabel;
    @BindView(R.id.profile_user_password_label)
    TextView passwordLabel;
    @BindView(R.id.user_profile_gender_icon)
    ImageView genderIcon;
    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindView(R.id.profile_edit_email)
    ImageView editEmail;
    @BindView(R.id.profile_edit_password)
    ImageView changePasswordButton;

    @BindView(R.id.profile_edit_marital_status)
    ImageView editMaritalStatus;
    @BindView(R.id.profile_user_marital_status_label)
    TextView maritalStatusLabel;

    @BindArray(R.array.marital_status_array)
    String[] maritalStatusArray;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private String currentMaritalStatus = "";

    private final Context context;

    public MyProfileView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.my_profile_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        initViews();
        enableDisableEmailEditText(false);
    }

    private void initViews() {
        VectorUtils.setVectorCompoundDrawable(sunLabel, context, R.drawable.leo_s, 0, 0, 0, R.color.black);
        VectorUtils.setVectorCompoundDrawable(dobLabel, context, R.drawable.ic_date_range_black_24dp, 0, 0, 0, R.color.black);
        VectorUtils.setVectorCompoundDrawable(timeOfBirthLabel, context, R.drawable.ic_access_time_black_24dp, 0, 0, 0, R.color.black);
        VectorUtils.setVectorCompoundDrawable(placeOfBirthLabel, context, R.drawable.ic_pin_drop_black_24dp, 0, 0, 0, R.color.black);
        VectorUtils.setVectorCompoundDrawable(mobileLabel, context, R.drawable.ic_phone_android_black_24dp, 0, 0, 0, R.color.black);
        VectorUtils.setVectorCompoundDrawable(emailLabel, context, R.drawable.ic_email_black_24dp, 0, 0, 0, R.color.black);
        VectorUtils.setVectorCompoundDrawable(passwordLabel, context, R.drawable.ic_lock_black_24dp, 0, 0, 0, R.color.black);
        VectorUtils.setVectorCompoundDrawable(maritalStatusLabel, context, R.drawable.icon_relationship, 0, 0, 0, R.color.black);


        editProfilePicButton.setBackground(ImageUtils.changeShapeColor(context, R.drawable.circle_bg, R.color.colorPrimary));

    }

    public void setUpUserInfo(PreferenceManger preferenceManger) {
        if (preferenceManger == null)
            return;
        LoginResponseModel loginResponseModel = preferenceManger.getUserDetails();
        if (loginResponseModel != null) {
            ImageUtils.loadImageUrl(context, profileImageView, R.drawable.icon_default_profile, loginResponseModel.getUserProfile().getProfileImage());
            // sunLabel.setText(loginResponseModel.getUserProfile().getStarSign());
            /* VectorUtils.setVectorCompoundDrawable(sunLabel, context, AppUtils.getSunSignIcon(loginResponseModel.getUserProfile().getStarSign()), 0, 0, 0, R.color.black);*/
            dobLabel.setText(DateUtils.getDateWithSuffix(DateUtils.parseStringDate(loginResponseModel.getUserProfile().getDob(), DateUtils.SERVER_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT)));
            timeOfBirthLabel.setText(DateUtils.getDOBTime(loginResponseModel.getUserProfile().getDob()));
            mobileLabel.setText("+" + loginResponseModel.getUserProfile().getCountryCode() + "-" + loginResponseModel.getUserProfile().getMobileNumber());
            emailLabel.setText(loginResponseModel.getUserProfile().getEmail());
            placeOfBirthLabel.setText(loginResponseModel.getUserProfile().getLocation());
            userName.setText(loginResponseModel.getUserProfile().getFullName());
            ageLabel.setText(DateUtils.getAge(loginResponseModel.getUserProfile().getDob(), DateUtils.SERVER_DATE_FORMAT) + " | ");
            ImageUtils.setDrawableImage(context, genderIcon, AppUtils.getGenderIcon(loginResponseModel.getUserProfile().getGender()));
            ImageUtils.changeImageColor(genderIcon, context, R.color.white);
            updateMaritalStatus(loginResponseModel.getUserProfile().getMaritalStatus());
        }
    }

    public void updateMaritalStatus(String maritalStatus) {
        currentMaritalStatus = maritalStatus;
        maritalStatusLabel.setText(currentMaritalStatus);
    }

    public Observable<Object> observeEditImageButtonClick() {
        return RxView.clicks(editProfilePicButton);
    }

    public Observable<Object> observeEditEmail() {
        return RxView.clicks(editEmail);
    }

    public Observable<Object> observeChangePasswordButton() {
        return RxView.clicks(changePasswordButton);
    }

    public Observable<Object> observeEditMaritalStatus() {
        return RxView.clicks(editMaritalStatus);
    }

    public void displayImage(Uri imageURI) {
        GlideApp.with(context).load(imageURI)
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(profileImageView);
    }


    public void enableDisableEmailEditText(boolean enable) {
        emailLabel.setEnabled(enable);
        if (enable) {
            editEmail.setImageResource(R.drawable.ic_done_black_24dp);
        } else {
            editEmail.setImageResource(R.drawable.ic_edit_black_24dp);
        }
    }

    public boolean isEmailEnabled() {
        if (emailLabel.isEnabled()) {
            // enableDisableEmailEditText(false);
            return true;
        } else {
            enableDisableEmailEditText(true);
            emailLabel.setSelection(getEmailId().length());
            return false;
        }
    }

    public boolean isEmailValid() {
        if (TextUtils.isEmpty(getEmailId())) {
            ToastUtils.shortToast("Please enter email id");
            return false;
        } else if (!MethodConstants.emailValidator(getEmailId())) {
            ToastUtils.shortToast("Please enter valid email id");
            return false;
        } else {
            return true;
        }
    }

    public String getEmailId() {
        return emailLabel.getText().toString().trim();
    }

    public String[] getMaritalStatusArray() {
        return maritalStatusArray;
    }

    public String getCurrentMaritalStatus() {
        return currentMaritalStatus;
    }

    public int getCheckedItem() {
        return StringUtils.getCurrentSelectedItemFromStringArray(maritalStatusArray, currentMaritalStatus);
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
