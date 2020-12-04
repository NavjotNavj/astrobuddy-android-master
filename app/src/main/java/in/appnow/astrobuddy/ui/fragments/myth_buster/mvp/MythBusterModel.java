package in.appnow.astrobuddy.ui.fragments.myth_buster.mvp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.MythBuster;
import in.appnow.astrobuddy.rest.response.MythBusterResponse;
import in.appnow.astrobuddy.ui.activities.youtube_player.YoutubePlayerActivity;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.MythBusterDetailFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 16:03, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public MythBusterModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<MythBusterResponse> getMythBusters(BaseRequestModel baseRequestModel) {
        return apiInterface.getMythBusters(baseRequestModel);
    }

    public void openYouTubeActivity(String videoId) {
        //start youtube player activity by passing selected video id via intent
        appCompatActivity.startActivity(new Intent(appCompatActivity, YoutubePlayerActivity.class)
                .putExtra("video_id", videoId));
    }

    public void replaceMythBusterDetailFragment(MythBuster mythBuster) {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, MythBusterDetailFragment.newInstance(mythBuster),FragmentUtils.MYTH_BUSTER_DETAIL_FRAGMENT);
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar(){
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }
    public void hideProgressBar(){
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }
}
