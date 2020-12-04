package in.appnow.astrobuddy.ui.fragments.horoscope_detail;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.SunSignModel;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.mvp.HoroScopeDetailModel;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.mvp.HoroScopeDetailPresenter;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.mvp.view.HoroScopeDetailView;

/**
 * Created by Abhishek Thanvi on 24/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class HoroscopeDetailFragment extends BaseFragment {

    @Inject
    APIInterface apiInterface;
    @Inject
    PreferenceManger preferenceManger;
    @Inject
    ABDatabase abDatabase;
    // @Inject
    HoroScopeDetailView horoScopeDetailView;
    // @Inject
    HoroScopeDetailPresenter horoScopeDetailPresenter;

    HoroScopeDetailModel model;

    private static final String ARG_SIGN = "sun_sign_data";
    private SunSignModel sunSignModel;

    public static HoroscopeDetailFragment newInstance(SunSignModel sunSignModel) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_SIGN, sunSignModel);
        HoroscopeDetailFragment fragment = new HoroscopeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sunSignModel = getArguments().getParcelable(ARG_SIGN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        horoScopeDetailView = new HoroScopeDetailView(getActivity());
        model = new HoroScopeDetailModel((AppCompatActivity) getContext(), apiInterface);
        horoScopeDetailPresenter = new HoroScopeDetailPresenter(horoScopeDetailView, model,
                preferenceManger, abDatabase);
        horoScopeDetailPresenter.setSunSignModel(sunSignModel);
        horoScopeDetailPresenter.onCreate();
        // showHintIfNotShown();
        return horoScopeDetailView;
    }

    private void showHintIfNotShown() {
        if (preferenceManger.getBooleanValue(PreferenceManger.HOROSCOPE_TAP_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.HOROSCOPE_BACK_PRESS_TAP_HINT)) {
            showToolbarBackPrompt(R.drawable.ic_action_navigation_white);
            preferenceManger.putBoolean(PreferenceManger.HOROSCOPE_BACK_PRESS_TAP_HINT, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        horoScopeDetailPresenter.onDestroy();
    }

}
