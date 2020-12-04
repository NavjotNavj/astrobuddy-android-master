package in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.ChatSampleResponse;
import in.appnow.astrobuddy.rest.response.SubscriptionPlanResponse;

/**
 * Created by sonu on 18:56, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatTopicsAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ChatSampleResponse.ChatSample> chatSampleList = new ArrayList<>(0);

    public ChatTopicsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_topics_row_layout, parent, false);
        ChatTopicsViewHolder chatTopicsViewHolder = new ChatTopicsViewHolder(view);

        return chatTopicsViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatTopicsViewHolder) {
            ((ChatTopicsViewHolder) holder).onBindData(context,chatSampleList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatSampleList.size();
    }

    public void swapData(List<ChatSampleResponse.ChatSample> subscriptionPlansArrayList) {
        this.chatSampleList.clear();
        if (subscriptionPlansArrayList != null && !subscriptionPlansArrayList.isEmpty()) {
            this.chatSampleList.addAll(subscriptionPlansArrayList);
        }
        notifyDataSetChanged();
    }

}
