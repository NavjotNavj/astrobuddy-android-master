package in.appnow.astrobuddy.ui.fragments.promo_template.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.base.BaseModel;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import io.reactivex.Observable;

/**
 * Created by sonu on 10:37, 11/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PromoTemplateModel {
    private final AppCompatActivity appCompatActivity;
    private APIInterface apiInterface;

    public PromoTemplateModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<MyAccountResponse> getTopics(BaseRequestModel baseRequestModel) {
        return apiInterface.getMyAccountDetails(baseRequestModel);
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }
}
