package in.appnow.astrobuddy.ui.fragments.myaccount.mvp.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.ChatHistoryResponse;
import in.appnow.astrobuddy.rest.response.TransactionHistoryResponse;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 13:20, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TransactionHistoryAdapter extends RecyclerView.Adapter {
    private final List<TransactionHistoryResponse.TransactionHistory> transactionHistoryModelList = new ArrayList<>(0);

    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private APIInterface apiInterface;

    public void setApiInterface(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_transaction_custom_row_layout, parent, false);
        TransactionHistoryViewHolder viewHolder = new TransactionHistoryViewHolder(parent.getContext(), view, userId, apiInterface);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TransactionHistoryResponse.TransactionHistory transactionHistoryModel = transactionHistoryModelList.get(position);
        ((TransactionHistoryViewHolder) holder).bindData(transactionHistoryModel);
    }

    @Override
    public int getItemCount() {
        return transactionHistoryModelList.size();
    }

    public void swapData(List<TransactionHistoryResponse.TransactionHistory> transactionHistoryModelList) {
        this.transactionHistoryModelList.clear();
        if (transactionHistoryModelList != null && !transactionHistoryModelList.isEmpty()) {
            this.transactionHistoryModelList.addAll(transactionHistoryModelList);
        }
        notifyDataSetChanged();
    }
}
