package in.appnow.astrobuddy.ui.activities.promo_code.mvp.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.PromoCodeResponse;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 15:18, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodeAdapter extends RecyclerView.Adapter {
    private List<PromoCodeResponse.PromoCodes> promoCodesArrayList = new ArrayList<>();
    private PublishSubject<PromoCodeResponse.PromoCodes> codesPublishSubject = PublishSubject.create();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_promo_code_recycler, parent, false);
        PromoCodeViewHolder holder = new PromoCodeViewHolder(view);
        RxView.clicks(holder.applyPromoCodeButton)
                .map(model -> promoCodesArrayList.get(holder.getAdapterPosition()))
                .subscribe(codesPublishSubject);
        return holder;
    }

    public PublishSubject<PromoCodeResponse.PromoCodes> getCodesPublishSubject() {
        return codesPublishSubject;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoCodeViewHolder) {
            ((PromoCodeViewHolder) holder).bindData(promoCodesArrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return promoCodesArrayList.size();
    }

    public void swapData(List<PromoCodeResponse.PromoCodes> promoCodesArrayList) {
        this.promoCodesArrayList.clear();
        if (promoCodesArrayList != null && !promoCodesArrayList.isEmpty()) {
            this.promoCodesArrayList.addAll(promoCodesArrayList);
        }
        notifyDataSetChanged();
    }
}
