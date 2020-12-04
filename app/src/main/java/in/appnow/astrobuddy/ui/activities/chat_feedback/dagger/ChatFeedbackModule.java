package in.appnow.astrobuddy.ui.activities.chat_feedback.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.chat_feedback.mvp.ChatFeedbackModel;
import in.appnow.astrobuddy.ui.activities.chat_feedback.mvp.ChatFeedbackPresenter;
import in.appnow.astrobuddy.ui.activities.chat_feedback.mvp.ChatFeedbackView;

/**
 * Created by sonu on 17:25, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class ChatFeedbackModule {

    private final AppCompatActivity appCompatActivity;

    public ChatFeedbackModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @ChatFeedbackScope
    public ChatFeedbackView chatFeedbackView() {
        return new ChatFeedbackView(appCompatActivity);
    }

    @Provides
    @ChatFeedbackScope
    public ChatFeedbackModel chatFeedbackModel(APIInterface apiInterface) {
        return new ChatFeedbackModel(appCompatActivity, apiInterface);
    }

    @Provides
    @ChatFeedbackScope
    public ChatFeedbackPresenter chatFeedbackPresenter(ChatFeedbackView view, ChatFeedbackModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ChatFeedbackPresenter(view, model, preferenceManger, abDatabase);
    }
}
