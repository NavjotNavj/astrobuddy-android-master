package in.appnow.astrobuddy.conversation_module.chat_history.mvp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.Conversations;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 13:20, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryViewHolder> {
    private final List<Conversations> conversationsList = new ArrayList<>(0);
    private PublishSubject<Conversations> itemViewClickSubject = PublishSubject.create();

    private Context context;

    public ChatHistoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ChatHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_history_row_layout, parent, false);
        ChatHistoryViewHolder viewHolder = new ChatHistoryViewHolder(context,view);
        RxView.clicks(viewHolder.viewAllButton)
                .map(model -> conversationsList.get(viewHolder.getAdapterPosition()))
                .subscribe(itemViewClickSubject);
        return viewHolder;
    }

    public PublishSubject<Conversations> getItemViewClickSubject() {
        return itemViewClickSubject;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHistoryViewHolder holder, int position) {
        Conversations conversations = conversationsList.get(position);
        holder.bindData(conversations);
    }

    @Override
    public int getItemCount() {
        return conversationsList.size();
    }

    public void swapData(List<Conversations> conversationsList) {
        this.conversationsList.clear();
        if (conversationsList != null && !conversationsList.isEmpty()) {
            this.conversationsList.addAll(conversationsList);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        itemViewClickSubject.onComplete(); //here we avoid memory leaks
    }
}
