package in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.PlanResponse;
import in.appnow.astrobuddy.rest.response.SubscriptionPlanResponse;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 18:56, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpgradePlanAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<PlanResponse.Plans> subscriptionPlansArrayList = new ArrayList<>(0);
    private PublishSubject<PlanResponse.Plans> plansPublishSubject = PublishSubject.create();
    private PublishSubject<Integer> showMoreLessPublishSubject = PublishSubject.create();
    private String currency;
    private int selectedPosition = -1;

    public UpgradePlanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upgrade_plan_custom_row_layuout, parent, false);
        UpgradePlanViewHolder upgradePlanViewHolder = new UpgradePlanViewHolder(view);
        RxView.clicks(upgradePlanViewHolder.buyNowButton)
                .map(model -> subscriptionPlansArrayList.get(upgradePlanViewHolder.getAdapterPosition()))
                .subscribe(plansPublishSubject);
        RxView.clicks(upgradePlanViewHolder.moreLessButton)
                .map(model -> upgradePlanViewHolder.getAdapterPosition())
                .subscribe(showMoreLessPublishSubject);
        return upgradePlanViewHolder;
    }

    public PublishSubject<PlanResponse.Plans> getPlansPublishSubject() {
        return plansPublishSubject;
    }

    public PublishSubject<Integer> getShowMoreLessPublishSubject() {
        return showMoreLessPublishSubject;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UpgradePlanViewHolder) {
            ((UpgradePlanViewHolder) holder).onBindData(subscriptionPlansArrayList.get(position), selectedPosition);
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionPlansArrayList.size();
    }
/*
    public void swapData(List<SubscriptionPlanResponse.SubscriptionPlans> subscriptionPlansArrayList, String currency) {
        this.currency = currency;
        this.subscriptionPlansArrayList.clear();
        if (subscriptionPlansArrayList != null && !subscriptionPlansArrayList.isEmpty()) {
            this.subscriptionPlansArrayList.addAll(subscriptionPlansArrayList);
        }
        notifyDataSetChanged();
    }*/

    public void swapData(List<PlanResponse.Plans> plansList, String currency) {
        this.currency = currency;
        this.subscriptionPlansArrayList.clear();
        if (plansList != null && !plansList.isEmpty()) {
            this.subscriptionPlansArrayList.addAll(plansList);
        }
        notifyDataSetChanged();
    }

    public void showHide(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }
}
