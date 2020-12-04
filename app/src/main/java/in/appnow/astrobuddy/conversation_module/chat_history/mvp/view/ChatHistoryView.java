package in.appnow.astrobuddy.conversation_module.chat_history.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.ChatHistoryResponse;
import in.appnow.astrobuddy.rest.response.Conversations;
import in.appnow.astrobuddy.utils.ImageUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 13:57, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryView extends BaseViewClass implements BaseView {

    @BindView(R.id.chat_history_recycler_view)
    RecyclerView chatRecyclerView;
    @BindView(R.id.no_chat_history_available)
    TextView noChatHistoryLabel;
    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private final ChatHistoryAdapter adapter = new ChatHistoryAdapter(getContext());

    public ChatHistoryView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.chat_history_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setNestedScrollingEnabled(false);
        chatRecyclerView.setAdapter(adapter);
    }

    private void showHideView(boolean isListEmpty, String message) {
        if (isListEmpty) {
            noChatHistoryLabel.setText(message);
            noChatHistoryLabel.setVisibility(View.VISIBLE);
            chatRecyclerView.setVisibility(View.GONE);
        } else {
            noChatHistoryLabel.setVisibility(View.GONE);
            chatRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public Observable<Conversations> onViewAllChats() {
        return adapter.getItemViewClickSubject();
    }

    public void loadChats(ChatHistoryResponse data) {
        if (data != null) {
            if (data.isErrorStatus()) {
                showHideView(true, data.getErrorMessage());
            } else {
                if (data.getConversationCount() > 0 && data.getConversationsList() != null && data.getConversationsList().size() > 0) {
                    showHideView(false, "");
                    adapter.swapData(data.getConversationsList());
                } else {
                    showHideView(true, "No chat history available.");
                }
            }
        } else {
            showHideView(true, "No chat history available.");
        }
    }

    @Override
    public void onUnknownError(String error) {
        showHideView(true, error);
    }

    @Override
    public void onTimeout() {
        showHideView(true, unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        showHideView(true, unknownErrorString);

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        showHideView(true, unknownErrorString);

    }


}
