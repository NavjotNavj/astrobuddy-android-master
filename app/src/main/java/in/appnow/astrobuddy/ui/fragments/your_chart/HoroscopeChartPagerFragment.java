package in.appnow.astrobuddy.ui.fragments.your_chart;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.AstrologyBaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.your_chart.mvp.HoroscopeChartPagerPresenter;
import in.appnow.astrobuddy.ui.fragments.your_chart.mvp.HoroscopeChartPagerView;

/**
 * Created by sonu on 17:32, 06/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeChartPagerFragment extends AstrologyBaseFragment {

    @Inject
    HoroscopeChartPagerView view;
    @Inject
    HoroscopeChartPagerPresenter presenter;
    @Inject
    ABDatabase abDatabase;

    public static HoroscopeChartPagerFragment newInstance() {

        Bundle args = new Bundle();

        HoroscopeChartPagerFragment fragment = new HoroscopeChartPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showHideBackButton(true);
            updateToolbarTitle("My Chart");
            showHideToolbar(true);
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        presenter.onCreate();
        presenter.setUpViewPager(getChildFragmentManager());
        //checkIfBackPromptIsShown();
        if (presenter.preferenceManger != null) {
            invalidateOptionMenu(presenter.preferenceManger.getStringValue(PreferenceManger.ASTROLOGY_API_LANGUAGE));
        }
        return view;
    }

    private void checkIfBackPromptIsShown() {
        if (presenter != null) {
            PreferenceManger preferenceManger = presenter.getPreferenceManger();
            if (preferenceManger != null) {
                if (preferenceManger.getBooleanValue(PreferenceManger.CHART_TAP_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.CHART_BACK_PRESS_TAP_HINT)) {
                    showToolbarBackPrompt(R.drawable.ic_action_navigation_white);
                    preferenceManger.putBoolean(PreferenceManger.CHART_BACK_PRESS_TAP_HINT, true);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setStartTimeMills(abDatabase,Config.CHART_ACTION);
    }

    @Override
    public void onPause() {
        super.onPause();
        setEndTimeMills(abDatabase,Config.CHART_ACTION);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onDetach() {
        // showHideBackButton(false);
        super.onDetach();
    }

    @Override
    public void onLanguageSelected(String language) {
        presenter.updateLanguage(language);
        presenter.setUpViewPager(getChildFragmentManager());
    }
}
