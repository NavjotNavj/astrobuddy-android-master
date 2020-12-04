package in.appnow.astrobuddy.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.ui.activities.intro.IntroModel;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import in.appnow.astrobuddy.utils.VectorUtils;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 18:11, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class BannerAdapter extends PagerAdapter {
    private final List<PromoBannerResponse.PromoBanner> promoBannerList = new ArrayList<>(0);
    private PublishSubject<PromoBannerResponse.PromoBanner> promoBannerPublishSubject = PublishSubject.create();

    private final Context context;
    private LayoutInflater layoutInflater;

    public BannerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return promoBannerList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = layoutInflater.inflate(R.layout.adapter_home_banner, view, false);
        PromoBannerResponse.PromoBanner model = promoBannerList.get(position);
        ImageView bannerImageView = imageLayout.findViewById(R.id.banner_image_view);
        TextView adLabel = imageLayout.findViewById(R.id.banner_ads_label);
        TextView clickButton = imageLayout.findViewById(R.id.banner_click_button);

        ImageUtils.loadImageUrl(context, bannerImageView, R.drawable.placeholder, model.getImage());
        RxView.clicks(bannerImageView)
                .map(data -> model)
                .subscribe(promoBannerPublishSubject);

        if (model.getType().equalsIgnoreCase(Config.AD_TYPE)) {
            adLabel.setVisibility(View.VISIBLE);
            VectorUtils.setVectorCompoundDrawable(adLabel, context, 0, 0, R.drawable.ic_info_black_18dp, 0, R.color.dark_grey);
            adLabel.setOnClickListener(view1 -> ToastUtils.shortToast("This is sponsored content."));
        } else {
            adLabel.setVisibility(View.GONE);
        }

        if (model.getAction().equalsIgnoreCase(Config.ACTION_NONE)){
            clickButton.setVisibility(View.GONE);
        }else{
            clickButton.setVisibility(View.VISIBLE);
        }
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    public PublishSubject<PromoBannerResponse.PromoBanner> getPromoBannerPublishSubject() {
        return promoBannerPublishSubject;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public void swapData(List<PromoBannerResponse.PromoBanner> promoBannerList) {
        if (promoBannerList != null && promoBannerList.size() > 0) {
            this.promoBannerList.clear();
            this.promoBannerList.addAll(promoBannerList);
            notifyDataSetChanged();
        }
    }

}
