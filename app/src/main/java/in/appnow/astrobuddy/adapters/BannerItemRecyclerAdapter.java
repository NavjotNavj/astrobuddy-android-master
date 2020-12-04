package in.appnow.astrobuddy.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.utils.DeviceUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class BannerItemRecyclerAdapter extends RecyclerView.Adapter<BannerItemRecyclerAdapter.BannerVH> {

    private List<PromoBannerResponse.PromoBanner> promoBannerList = new ArrayList<>(0);
    private PublishSubject<PromoBannerResponse.PromoBanner> promoBannerPublishSubject = PublishSubject.create();
    private Context context;

    public BannerItemRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BannerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_banner, parent, false);
        BannerVH vh = new BannerVH(view);
        RxView.clicks(vh.itemView)
                .map(model -> promoBannerList.get(vh.getAdapterPosition()))
                .subscribe(promoBannerPublishSubject);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BannerVH holder, int position) {
        holder.bindData(promoBannerList.get(position));
    }

    public PublishSubject<PromoBannerResponse.PromoBanner> getPromoBannerPublishSubject() {
        return promoBannerPublishSubject;
    }


    @Override
    public int getItemCount() {
        return promoBannerList.size();
    }

    class BannerVH extends RecyclerView.ViewHolder {

        @BindView(R.id.banner_image_view)
        AppCompatImageView bannerImageView;

        @BindView(R.id.main_item)
        RelativeLayout mainItem;

        public BannerVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(PromoBannerResponse.PromoBanner promoBanner) {
            ImageUtils.loadImageUrl(context, bannerImageView, R.drawable.placeholder, promoBanner.getImage());
        }
    }
}
