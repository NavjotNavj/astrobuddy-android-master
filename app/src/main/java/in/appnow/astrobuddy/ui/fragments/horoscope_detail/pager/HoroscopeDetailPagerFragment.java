package in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerPresenter;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerView;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by Abhishek Thanvi on 24/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class HoroscopeDetailPagerFragment extends BaseFragment {

    private static final String TAG = HoroscopeDetailPagerFragment.class.getSimpleName();
    @Inject
    HoroscopePagerView horoscopePagerView;
    @Inject
    HoroscopePagerPresenter horoscopePagerPresenter;
    @Inject
    ABDatabase abDatabase;

    private static final String ARG_SIGN_POSITION = "sun_sign_position";
    private int position;

    public static HoroscopeDetailPagerFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_SIGN_POSITION, position);
        HoroscopeDetailPagerFragment fragment = new HoroscopeDetailPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
        showHideBackButton(true);
            showHideToolbar(true);
        updateToolbarTitle("Prediction");
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_SIGN_POSITION, 0);
            Logger.DebugLog(TAG,"Position : "+position);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        horoscopePagerPresenter.onCreate();
        horoscopePagerView.setUpPager(this,position);
        Logger.DebugLog("HOROSOCOPE DETAIL","ON CREATE VIEW");
        return horoscopePagerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setStartTimeMills(abDatabase,Config.FORECAST_DETAIL_ACTION);
    }

    @Override
    public void onPause() {
        super.onPause();
        setEndTimeMills(abDatabase,Config.FORECAST_DETAIL_ACTION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        horoscopePagerPresenter.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
