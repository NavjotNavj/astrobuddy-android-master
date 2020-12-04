package in.appnow.astrobuddy.ui.fragments.promo_template.mvp;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 10:35, 11/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PromoTemplateView extends BaseViewClass implements BaseView {
    @BindView(R.id.promo_template_add_topics_button)
    Button addTopicsButton;
    @BindView(R.id.promo_template_image_view)
    ImageView imageView;
    @BindView(R.id.promo_template_label)
    TextView descriptionLabel;
    public FragmentManager fragmentManager;

    public PromoTemplateView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.promo_template_fragment, this);
        ButterKnife.bind(this, this);
    }

    public Observable<Object> observeAddTopicsButtonClick() {
        return RxView.clicks(addTopicsButton);
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, fragment, fragmentTag);
    }

    @Override
    public void onUnknownError(String error) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {

    }


    public void updateView(@NonNull PromoBannerResponse.PromoBanner promoBanner) {
        ImageUtils.loadImageUrl(getContext(), imageView, R.drawable.placeholder, promoBanner.getImage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionLabel.setText(Html.fromHtml(promoBanner.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionLabel.setText(Html.fromHtml(promoBanner.getDescription()));
        }

        if (promoBanner.getAction().equalsIgnoreCase(Config.ADD_TOPICS_ACTION)) {
            addTopicsButton.setVisibility(View.VISIBLE);
        } else {
            addTopicsButton.setVisibility(View.GONE);
        }
    }
}
