package in.appnow.astrobuddy.ui.activities.intro.mvp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.activities.intro.IntroModel;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;

/**
 * Created by sonu on 17:17, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class IntroPagerModel {

    private final AppCompatActivity appCompatActivity;

    private static final int BANNER_IMAGES[] = {R.drawable.onboard_one, R.drawable.onboard_five, R.drawable.onboard_three, R.drawable.onboard_two};

    public IntroPagerModel(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public List<IntroModel> getIntroModel() {
        String introTitleArray[] = appCompatActivity.getResources().getStringArray(R.array.intro_title);
        String introDescArray[] = appCompatActivity.getResources().getStringArray(R.array.intro_desc);

        List<IntroModel> introModelList = new ArrayList<>();
        for (int i = 0; i < introDescArray.length; i++) {
            IntroModel model = new IntroModel(BANNER_IMAGES[i], introTitleArray[i], introDescArray[i]);
            introModelList.add(model);
        }
        return introModelList;
    }

    protected void launchActivity(boolean isLogin) {
        Intent intent;
        if (isLogin) {
            intent = new Intent(appCompatActivity, HomeActivity.class);
        } else {
            intent = new Intent(appCompatActivity, LoginActivity.class);

        }
        appCompatActivity.startActivity(intent);
        appCompatActivity.finish();
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
