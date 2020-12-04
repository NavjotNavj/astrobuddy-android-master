package in.appnow.astrobuddy.ui.fragments.referral.mvp;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.ReferralCodeResponse;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralPojo;
import in.appnow.astrobuddy.utils.DeviceUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 10:59, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ReferralModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    private static final int ICONS[] = {R.drawable.icon_whatsapp, R.drawable.icon_message, R.drawable.icon_gmail, R.drawable.facebook, R.drawable.twitter, R.drawable.icon_messenger, R.drawable.icon_telegram};


    public ReferralModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<ReferralCodeResponse> fetchReferralCode(BaseRequestModel requestModel){
        return apiInterface.getUserReferralCode(requestModel);
    }

    public List<ReferralPojo> getReferralList() {
        String appPackageName[] = appCompatActivity.getResources().getStringArray(R.array.sharing_package_array);
        List<ReferralPojo> referralPojoList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if (appPackageName[i].equalsIgnoreCase("com.message")) {
                ReferralPojo pojo = new ReferralPojo(appPackageName[i], ICONS[i]);
                referralPojoList.add(pojo);
            } else {
                if (DeviceUtils.isAppInstalled(appCompatActivity, appPackageName[i])) {
                    ReferralPojo pojo = new ReferralPojo(appPackageName[i], ICONS[i]);
                    referralPojoList.add(pojo);
                }
            }
        }
        ReferralPojo pojo = new ReferralPojo("More", R.drawable.icon_more);
        referralPojoList.add(pojo);
        return referralPojoList;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

}
