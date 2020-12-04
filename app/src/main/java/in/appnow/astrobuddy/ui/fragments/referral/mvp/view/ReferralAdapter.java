package in.appnow.astrobuddy.ui.fragments.referral.mvp.view;

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
import in.appnow.astrobuddy.ui.fragments.referral.ReferralPojo;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 13:20, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ReferralAdapter extends RecyclerView.Adapter<ReferralViewHolder> {
    private final List<ReferralPojo> referralPojoArrayList = new ArrayList<>(0);
    private PublishSubject<ReferralPojo> itemViewClickSubject = PublishSubject.create();

    private Context context;

    public ReferralAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ReferralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.referral_custom_layout, parent, false);
        ReferralViewHolder viewHolder = new ReferralViewHolder(view);
        RxView.clicks(viewHolder.itemView)
                .map(model -> referralPojoArrayList.get(viewHolder.getAdapterPosition()))
                .subscribe(itemViewClickSubject);
        return viewHolder;
    }

    public Observable<ReferralPojo> getItemViewClickSubject() {
        return itemViewClickSubject;
    }

    @Override
    public void onBindViewHolder(@NonNull ReferralViewHolder holder, int position) {
        ReferralPojo referralPojo = referralPojoArrayList.get(position);
        holder.bindData(context, referralPojo);
    }

    @Override
    public int getItemCount() {
        return referralPojoArrayList.size();
    }

    public void swapData(List<ReferralPojo> referralPojoArrayList) {
        this.referralPojoArrayList.clear();
        if (referralPojoArrayList != null && !referralPojoArrayList.isEmpty()) {
            this.referralPojoArrayList.addAll(referralPojoArrayList);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        itemViewClickSubject.onComplete(); //here we avoid memory leaks
    }
}
