package in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.response.Conversations;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by sonu on 13:57, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SingleChatHistoryView extends FrameLayout {

    @BindView(R.id.chat_history_recycler_view)
    RecyclerView chatRecyclerView;
    @BindView(R.id.no_chat_history_available)
    TextView noChatHistoryLabel;
    @BindView(R.id.background_image)
    ImageView backgroundImage;

    private final ChatHistoryConversationAdapter adapter = new ChatHistoryConversationAdapter(getContext());

    public SingleChatHistoryView(@NonNull Context context) {
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

    public void loadChats(Conversations conversations, PreferenceManger preferenceManger) {
        if (conversations != null && conversations.getMessageCount() > 0 && conversations.getMessagesList() != null && conversations.getMessagesList().size() > 0) {
            showHideView(false, "");
            adapter.setPreferenceManger(preferenceManger);
            adapter.swapData(conversations.getMessagesList());
        } else {
            showHideView(true, "No chats available.");
        }
    }
}
