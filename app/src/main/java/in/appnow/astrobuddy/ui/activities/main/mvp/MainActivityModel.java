package in.appnow.astrobuddy.ui.activities.main.mvp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.models.NavigationModel;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.StartChatRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 11:03, 13/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MainActivityModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;
    private static final int ICONS[] = {R.drawable.ic_pages_black_24dp, R.drawable.ic_question_answer_black_24dp, R.drawable.ic_photo_library_black_24dp, R.drawable.ic_insert_chart_black_24dp, R.drawable.ic_card_giftcard_black_24dp, R.drawable.ic_person_black_24dp, R.drawable.ic_share_black_24dp, R.drawable.ic_contact_phone_black_24dp, R.drawable.ic_power_settings_new_black_24dp};


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public MainActivityModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public List<NavigationModel> getNavigationList() {
        String navigationMenus[] = appCompatActivity.getResources().getStringArray(R.array.navigation_menu);
        List<NavigationModel> navigationModelList = new ArrayList<>();

        for (int i = 0; i < navigationMenus.length; i++) {
            NavigationModel navigationModel = new NavigationModel(ICONS[i], navigationMenus[i], "",false);
            navigationModelList.add(navigationModel);
        }

        return navigationModelList;
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
