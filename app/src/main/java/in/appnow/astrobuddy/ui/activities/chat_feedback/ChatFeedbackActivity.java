package in.appnow.astrobuddy.ui.activities.chat_feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.ui.activities.chat_feedback.dagger.ChatFeedbackModule;
import in.appnow.astrobuddy.ui.activities.chat_feedback.dagger.DaggerChatFeedbackComponent;
import in.appnow.astrobuddy.ui.activities.chat_feedback.mvp.ChatFeedbackPresenter;
import in.appnow.astrobuddy.ui.activities.chat_feedback.mvp.ChatFeedbackView;

/**
 * Created by sonu on 17:25, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackActivity extends BaseActivity {

    public static final String ARG_MESSAGE = "message";
    public static final String ARG_SESSION_ID = "session_id";
    public static final String ARG_FEEDBACK_TYPE = "feedback_type";
    public static final String ARG_REQUEST_CODE = "request_code";
    @Inject
    ChatFeedbackView view;
    @Inject
    ChatFeedbackPresenter presenter;

    public static void openChatFeedbackActivity(Context context,
                                                String sessionId,
                                                boolean isFinish,
                                                String message,
                                                String feedBackType,
                                                int requestCode) {
        Intent intent = new Intent(context, ChatFeedbackActivity.class);
        intent.putExtra(ARG_MESSAGE, message);
        intent.putExtra(ARG_SESSION_ID, sessionId);
        intent.putExtra(ARG_FEEDBACK_TYPE, feedBackType);
        intent.putExtra(ARG_REQUEST_CODE, requestCode);

        if (requestCode == -1) {
            context.startActivity(intent);

        } else {
            ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
        }
        if (isFinish)
            ((AppCompatActivity) context).finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerChatFeedbackComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .chatFeedbackModule(new ChatFeedbackModule(this))
                .build().inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
