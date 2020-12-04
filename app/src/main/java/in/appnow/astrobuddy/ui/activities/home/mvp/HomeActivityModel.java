package in.appnow.astrobuddy.ui.activities.home.mvp;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.PostUserStatsRequest;
import in.appnow.astrobuddy.rest.request.StartChatRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.rest.service.AstroService;
import in.appnow.astrobuddy.ui.common_activity.CommonActivity;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class HomeActivityModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public HomeActivityModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<StartChatResponse> startChat() {
        return apiInterface.startChat();
    }

    public Observable<BaseResponseModel> doLogout(BaseRequestModel model) {
        return apiInterface.doLogout(model);
    }

    public void startConversationActivity(int currentQueue, String sessionId, boolean isExistingChat) {
        ConversationActivity.openActivity(appCompatActivity, currentQueue, sessionId, isExistingChat);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void showAddTopicDialog(String message) {
        DialogHelperClass.showMessageOKCancel(appCompatActivity, message, "Add Topic", "Cancel", (dialogInterface, i) -> {
            FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, MyAccountFragment.newInstance(), FragmentUtils.MY_ACCOUNT_FRAGMENT);
        }, null);
    }

    public Observable<MyAccountResponse> getMyAccountDetails(BaseRequestModel baseRequestModel) {
        return apiInterface.getMyAccountDetails(baseRequestModel);
    }

    public void replaceAddTopicPointsFragment(int topicsCount) {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, AddCreditsFragment.newInstance(topicsCount), FragmentUtils.BUY_TOPICS_FRAGMENT);
    }

    public void onLogoutSuccess() {
        appCompatActivity.startActivity(new Intent(appCompatActivity, LoginActivity.class));
        appCompatActivity.finish();
    }
}
