package in.appnow.astrobuddy.ui.fragments.more.mvp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.models.NavigationModel;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 22/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class MoreSettingsModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    private static final int ICONS[] = {R.drawable.ic_person_black_24dp
            ,R.drawable.ic_contact_phone_black_24dp,
            R.drawable.ic_share_black_24dp,
            R.mipmap.ic_tod,
            R.drawable.ic_power_settings_new_black_24dp};


    public MoreSettingsModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public Observable<BaseResponseModel> doLogout(BaseRequestModel model) {
        return apiInterface.doLogout(model);
    }

    public List<NavigationModel> getMoreItemList() {
        String navigationMenus[] = appCompatActivity.getResources().getStringArray(R.array.more_menu);
        List<NavigationModel> navigationModelList = new ArrayList<>();

        for (int i = 0; i < navigationMenus.length; i++) {
            boolean isSwitch = false;
            if (i == 3) {
                isSwitch = true;
            }
            NavigationModel navigationModel = new NavigationModel(ICONS[i], navigationMenus[i], "", isSwitch);
            navigationModelList.add(navigationModel);
        }

        return navigationModelList;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }


    public void onLogoutSuccess() {
        appCompatActivity.startActivity(new Intent(appCompatActivity, LoginActivity.class));
        appCompatActivity.finish();
    }

}
