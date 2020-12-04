package in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp;

import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.SingleChatHistoryFragment;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;

/**
 * Created by sonu on 13:58, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SingleChatHistoryModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public SingleChatHistoryModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
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

    public ArrayList<Parcelable> getChatMessages(){
        return appCompatActivity.getIntent().getParcelableArrayListExtra(SingleChatHistoryFragment.CHAT_DATA);
    }
}
