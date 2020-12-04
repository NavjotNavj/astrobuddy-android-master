package in.appnow.astrobuddy.ui.fragments.horoscope.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.TipOfTheRequest;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.rest.response.TipOfTheDayResponse;
import in.appnow.astrobuddy.ui.activities.chat_feedback.ChatFeedbackActivity;
import in.appnow.astrobuddy.ui.fragments.call_plans.CallPlansFragment;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 11:16, 13/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public HoroscopeModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public void replaceChatTopicsFragment() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, ChatTopicsFragment.newInstance(true), FragmentUtils.CHAT_TOPICS_FORECAST_FRAGMENT);
    }

    public void replaceTalkAstroBuddyFragment() {
        // AppUtils.openWebLink(appCompatActivity,"https://www.astrobuddy.guru/schedual-call.php");
        FragmentUtils.replaceFragmentCommon(
                appCompatActivity.getSupportFragmentManager(),
                R.id.common_frame_container,
                CallPlansFragment.newInstance(),
                FragmentUtils.CALL_PLAN_FRAGMENT,
                false);

        /* CommonActivity.openCommonActivity(
                appCompatActivity,
                "Talk to AstroBuddy",
                FragmentUtils.TALK_FRAGMENT); */
    }

    public Observable<TipOfTheDayResponse> getTipOfTheDay(TipOfTheRequest request) {
        return apiInterface.getTipOfTheDay(request);
    }


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void openFeedbackActivity(String chatSessionId, String date) {
        ChatFeedbackActivity.openChatFeedbackActivity(appCompatActivity, chatSessionId, false, "Please give feedback for your previous chat on " + DateUtils.parseFeedBackDateFormat(date), FragmentUtils.FEEDBACK_TYPE_CHAT, -1);
    }

    public Observable<StartChatResponse> startChat() {
        return apiInterface.startChat();
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void startConversationActivity(int currentQueue, String sessionId, boolean isExistingChat) {
        ConversationActivity.openActivity(appCompatActivity, currentQueue, sessionId, isExistingChat);
    }

    public void showAddTopicDialog(String message) {
        DialogHelperClass.showMessageOKCancel(appCompatActivity, message, "Add Topic", "Cancel", (dialogInterface, i) -> {
            FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, MyAccountFragment.newInstance(), FragmentUtils.MY_ACCOUNT_FRAGMENT);
        }, null);
    }

    public void openCall() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, CallPlansFragment.newInstance(), FragmentUtils.CALL_PLAN_FRAGMENT);
        //CommonActivity.openCommonActivity(appCompatActivity, "Talk to AstroBuddy", FragmentUtils.TALK_FRAGMENT);

    }
}
