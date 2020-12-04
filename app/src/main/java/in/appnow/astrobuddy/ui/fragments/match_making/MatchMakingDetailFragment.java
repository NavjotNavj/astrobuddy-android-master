package in.appnow.astrobuddy.ui.fragments.match_making;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import in.appnow.astrobuddy.base.AstrologyBaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingDetailPresenter;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingDetailView;

/**
 * Created by sonu on 18:43, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingDetailFragment extends AstrologyBaseFragment {

    @Inject
    MatchMakingDetailView view;
    @Inject
    MatchMakingDetailPresenter presenter;
    @Inject
    ABDatabase abDatabase;

    public static MatchMakingDetailFragment newInstance(String dobMale, String tobMale, String latLngMale, String dobFemale, String tobFemale, String latLngFemale) {

        Bundle args = new Bundle();
        try {
            args.putString(MatchMakingDetailPagerFragment.ARG_DOB_MALE, dobMale);
            args.putString(MatchMakingDetailPagerFragment.ARG_TOB_MALE, tobMale);
            args.putString(MatchMakingDetailPagerFragment.ARG_LAT_LNG_MALE, latLngMale);
            args.putString(MatchMakingDetailPagerFragment.ARG_DOB_FEMALE, dobFemale);
            args.putString(MatchMakingDetailPagerFragment.ARG_TOB_FEMALE, tobFemale);
            args.putString(MatchMakingDetailPagerFragment.ARG_LAT_LNG_FEMALE, latLngFemale);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MatchMakingDetailFragment fragment = new MatchMakingDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String dobMale, tobMale, latLngMale, dobFemale, tobFemale, latLngFemale;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showHideBackButton(true);
            updateToolbarTitle("Match Making");
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
                if (getArguments().containsKey(MatchMakingDetailPagerFragment.ARG_DOB_MALE)) {
                    dobMale = getArguments().getString(MatchMakingDetailPagerFragment.ARG_DOB_MALE);
                }
                if (getArguments().containsKey(MatchMakingDetailPagerFragment.ARG_TOB_MALE)) {
                    tobMale = getArguments().getString(MatchMakingDetailPagerFragment.ARG_TOB_MALE);
                }
                if (getArguments().containsKey(MatchMakingDetailPagerFragment.ARG_LAT_LNG_MALE)) {
                    latLngMale = getArguments().getString(MatchMakingDetailPagerFragment.ARG_LAT_LNG_MALE);
                }
                if (getArguments().containsKey(MatchMakingDetailPagerFragment.ARG_DOB_FEMALE)) {
                    dobFemale = getArguments().getString(MatchMakingDetailPagerFragment.ARG_DOB_FEMALE);
                }
                if (getArguments().containsKey(MatchMakingDetailPagerFragment.ARG_TOB_FEMALE)) {
                    tobFemale = getArguments().getString(MatchMakingDetailPagerFragment.ARG_TOB_FEMALE);
                }
                if (getArguments().containsKey(MatchMakingDetailPagerFragment.ARG_LAT_LNG_FEMALE)) {
                    latLngFemale = getArguments().getString(MatchMakingDetailPagerFragment.ARG_LAT_LNG_FEMALE);
                }
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onLanguageSelected(String language) {
        presenter.updateLanguage(language);
        view.setUpViewPager(getChildFragmentManager(), dobMale, tobMale, latLngMale, dobFemale, tobFemale, latLngFemale);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        showHideBackButton(true);
        view.setUpViewPager(getChildFragmentManager(), dobMale, tobMale, latLngMale, dobFemale, tobFemale, latLngFemale);
        if (presenter.preferenceManger != null) {
            invalidateOptionMenu(presenter.preferenceManger.getStringValue(PreferenceManger.ASTROLOGY_API_LANGUAGE));
        }
        presenter.onCreate();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setStartTimeMills(abDatabase, Config.MATCH_MAKING_ACTION);
    }

    @Override
    public void onPause() {
        super.onPause();
        setEndTimeMills(abDatabase, Config.MATCH_MAKING_ACTION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
