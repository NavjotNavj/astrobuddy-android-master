package in.appnow.astrobuddy.ui.activities.chat_feedback.mvp;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CallFeedbackRequest;
import in.appnow.astrobuddy.rest.request.ChatFeedbackRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.ui.activities.chat_feedback.ChatFeedbackActivity;
import io.reactivex.Observable;

import static android.app.Activity.RESULT_OK;


/**
 * Created by sonu on 17:28, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public ChatFeedbackModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    /* public int getTopicsDebited() {
         return appCompatActivity.getIntent().getIntExtra(ChatFeedbackActivity.ARG_TOPIC, 0);
     }*/
    public Observable<BaseResponseModel> sendFeedback(BaseRequestModel request) {
        if (request instanceof ChatFeedbackRequest) {
            return apiInterface.submitChatFeedback((ChatFeedbackRequest) request);
        } else {
            return apiInterface.submitCallFeeedBack((CallFeedbackRequest) request);
        }
    }

    /*public Observable<BaseResponseModel> sendCallFeedback(CallFeedbackRequest request) {
        return apiInterface.submitCallFeeedBack(request);
    }*/

    public String getSessionId() {
        return appCompatActivity.getIntent().getStringExtra(ChatFeedbackActivity.ARG_SESSION_ID);
    }

    public String getFeedBackType() {
        return appCompatActivity.getIntent().getStringExtra(ChatFeedbackActivity.ARG_FEEDBACK_TYPE);
    }

    public String getMessage() {
        return appCompatActivity.getIntent().getStringExtra(ChatFeedbackActivity.ARG_MESSAGE);
    }

    public int getRequestCode() {
        return appCompatActivity.getIntent().getIntExtra(ChatFeedbackActivity.ARG_REQUEST_CODE, -1);
    }

    public void closeActivity() {
        if (getRequestCode() != -1) {
            appCompatActivity.setResult(RESULT_OK, new Intent());
        }
        appCompatActivity.finish();
        //appCompatActivity.onBackPressed();
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgress() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgress() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }
}
