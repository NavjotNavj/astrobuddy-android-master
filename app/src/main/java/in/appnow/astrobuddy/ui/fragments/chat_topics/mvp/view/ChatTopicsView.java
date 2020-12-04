package in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.ChatSampleResponse;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 15:44, 17/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatTopicsView extends BaseViewClass implements BaseView {

    @BindView(R.id.chat_topics_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.chat_topics_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_chat_topics_available)
    TextView noTopicLabel;
    @BindView(R.id.start_chat_button)
    Button startChatButton;
    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindString(R.string.unknown_error)
    String unknownErrorString;
    private final ChatTopicsAdapter adapter = new ChatTopicsAdapter(getContext());

    public ChatTopicsView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.chat_topics_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        setUpRecyclerView();
    }

    public void showHideProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
        if (visibility == VISIBLE) {
            noTopicLabel.setVisibility(View.GONE);
        }
    }

    private void showHideView(boolean isListEmpty, String message) {
        if (isListEmpty) {
            noTopicLabel.setText(message);
            noTopicLabel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noTopicLabel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public Observable<Object> observeStartChatButtonClick() {
        return RxView.clicks(startChatButton);
    }

    public void updateView(ChatSampleResponse response) {
        if (response.isErrorStatus()) {
            showHideView(true, response.getErrorMessage());
        } else {
            if (response.getChatSampleList() != null && response.getChatSampleList().size() > 0) {
                showHideView(false, "");
                adapter.swapData(response.getChatSampleList());
            } else {
                showHideView(true, "No chat topics available.");
            }
        }
    }

    @Override
    public void onUnknownError(String error) {
        ToastUtils.shortToast(error);
    }

    @Override
    public void onTimeout() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        ToastUtils.shortToast(unknownErrorString);
    }


}
