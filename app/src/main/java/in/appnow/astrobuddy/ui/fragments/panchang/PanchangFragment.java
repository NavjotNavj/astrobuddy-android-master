package in.appnow.astrobuddy.ui.fragments.panchang;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.base.AstrologyBaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.panchang.mvp.PanchangPresenter;
import in.appnow.astrobuddy.ui.fragments.panchang.mvp.PanchangView;

/**
 * Created by sonu on 13:08, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PanchangFragment extends AstrologyBaseFragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    private static final String ARG_LATLNG = "lat_lng";
    private static final String ARG_LOCATION = "location";

    @Inject
    PanchangView view;
    @Inject
    PanchangPresenter presenter;
    @Inject
    ABDatabase abDatabase;

    private String date, time, location, latLng;

    public static PanchangFragment newInstance(String date, String time, String location, String latLng) {

        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_TIME, time);
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_LATLNG, latLng);
        PanchangFragment fragment = new PanchangFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showHideBackButton(true);
            updateToolbarTitle("Panchang");
            showHideToolbar(true);
            hideBottomBar(false);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                date = getArguments().getString(ARG_DATE);
                time = getArguments().getString(ARG_TIME);
                location = getArguments().getString(ARG_LOCATION);
                latLng = getArguments().getString(ARG_LATLNG);
            }
        }
        catch (Exception ignored){

        }
    }

    @Override
    public void onLanguageSelected(String language) {
        fetchPanchang(language);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        showHideBackButton(true);
        presenter.onCreate();
        if (presenter.preferenceManger != null) {
            invalidateOptionMenu(presenter.preferenceManger.getStringValue(PreferenceManger.ASTROLOGY_API_LANGUAGE));
        }
        presenter.fetchPanchangDetails(date, time, location, latLng);
        return view;
    }

    private void fetchPanchang(String language) {
        if (presenter != null) {
            presenter.fetchPanchang(date, time, latLng, language);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setStartTimeMills(abDatabase,Config.PANCHANG_ACTION);
    }

    @Override
    public void onPause() {
        super.onPause();
        setEndTimeMills(abDatabase,Config.PANCHANG_ACTION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
