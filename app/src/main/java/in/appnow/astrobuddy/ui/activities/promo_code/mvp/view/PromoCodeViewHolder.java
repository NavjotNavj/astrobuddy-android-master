package in.appnow.astrobuddy.ui.activities.promo_code.mvp.view;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.PromoCodeResponse;

/**
 * Created by sonu on 15:18, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodeViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.promoCodeTv)
    AppCompatTextView promoCodeLabel;
    @BindView(R.id.promoCodeDesc)
    AppCompatTextView promoCodeDescription;
    @BindView(R.id.apply_button)
    TextView applyPromoCodeButton;

    public PromoCodeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(PromoCodeResponse.PromoCodes promoCodes) {
        promoCodeLabel.setText(promoCodes.getPromoCode());
        promoCodeDescription.setText(promoCodes.getDescription());
    }
}
