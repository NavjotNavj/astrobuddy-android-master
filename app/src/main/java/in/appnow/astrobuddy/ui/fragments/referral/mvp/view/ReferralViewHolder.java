package in.appnow.astrobuddy.ui.fragments.referral.mvp.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.helper.glide.GlideApp;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralPojo;

/**
 * Created by sonu on 13:21, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ReferralViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.referral_row_image_view)
    ImageView referralImageView;

     ReferralViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bindData(Context context, ReferralPojo model) {
        GlideApp.with(context).load(model.getDrawableIcon())
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(referralImageView);
    }

}
