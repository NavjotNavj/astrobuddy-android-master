package in.appnow.astrobuddy.ui.fragments.home.mvp;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.models.HomeModel;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.ClickMetricsRequest;
import in.appnow.astrobuddy.rest.request.TipOfTheRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.rest.response.TipOfTheDayResponse;
import in.appnow.astrobuddy.ui.common_activity.CommonActivity;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class UserHomeModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    private static final int HOME_ICONS[] = {R.drawable.ic_insert_chart_white_24dp,
            R.drawable.home_match_making,
            R.drawable.home_forecast, R.drawable.home_myth_buster,
            R.drawable.home_video, R.drawable.home_panchang,
            R.drawable.home_my_account, R.drawable.home_call,
            R.drawable.home_astro_chat};

    private static final String[] HOME_TITLES = {"Kundli", "Match Making", "Prediction", "Myth Buster"
            , "Videos", "Panchang", "My Account", "Live Call", "Live Chat"};


    public UserHomeModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<MyAccountResponse> getTopics(BaseRequestModel baseRequestModel) {
        return apiInterface.getMyAccountDetails(baseRequestModel);
    }


    public Observable<PromoBannerResponse> getPromoBanners() {
        return apiInterface.getPromoBanners();
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

    public HomeModel getHomeList() {
        HomeModel homeModel = new HomeModel();
        homeModel.setHomeIcons(HOME_ICONS);
        homeModel.setHomeTitles(Arrays.asList(HOME_TITLES));
        return homeModel;
    }

    public Observable<TipOfTheDayResponse> getTipOfTheDay(TipOfTheRequest request) {
        return apiInterface.getTipOfTheDay(request);
    }


    public void openLink(String title, String url) {
        CommonActivity.openCommonActivity(appCompatActivity, title, url, FragmentUtils.BANNER_CLICK_FRAGMENT);
    }
}
