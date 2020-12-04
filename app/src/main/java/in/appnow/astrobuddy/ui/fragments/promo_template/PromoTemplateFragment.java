package in.appnow.astrobuddy.ui.fragments.promo_template;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.promo_template.mvp.PromoTemplatePresenter;
import in.appnow.astrobuddy.ui.fragments.promo_template.mvp.PromoTemplateView;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 10:34, 11/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PromoTemplateFragment extends BaseFragment {

    private static final String ARG_PROMO_BANNER = "promo_banner";
    Context context;

    @Inject
    PromoTemplateView view;
    @Inject
    PromoTemplatePresenter presenter;

    private PromoBannerResponse.PromoBanner promoBanner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            showHideToolbar(true);
            showHideBackButton(true);
        } catch (Exception ignored) {

        }
    }

    public static PromoTemplateFragment newInstance(PromoBannerResponse.PromoBanner promoBanner) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_PROMO_BANNER, promoBanner);
        PromoTemplateFragment fragment = new PromoTemplateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            promoBanner = getArguments().getParcelable(ARG_PROMO_BANNER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.DebugLog("PROMO TEMP","ON RESUMEr");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        view.fragmentManager = getActivity().getSupportFragmentManager();
        updateToolbarTitle(promoBanner.getTitle());
        view.updateView(promoBanner);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
