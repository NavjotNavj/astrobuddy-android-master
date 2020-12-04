package in.appnow.astrobuddy.ui.fragments.horoscope;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.OnDrawerOpenListener;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopeModel;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopePresenter;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopeView;

/**
 * Created by Abhishek Thanvi on 29/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class HoroScopeFragment extends BaseFragment implements OnDrawerOpenListener, HoroscopePresenter.OnHoroscopeTutorialListener {
    private static final String ARG_NOTIFICATION_CALL = "notification_call";
    // @Inject
    HoroscopeView view;
    // @Inject
    HoroscopePresenter presenter;

    HoroscopeModel horoscopeModel;

    @Inject
    APIInterface apiInterface;
    @Inject
    PreferenceManger preferenceManger;
    @Inject
    ABDatabase abDatabase;

    private Context context;
    private HoroscopePresenter.OnHoroscopeTutorialListener onHoroscopeTutorialListener;

    private boolean isNotificationCall;

    public static HoroScopeFragment newInstance(boolean isNotificationCall) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_NOTIFICATION_CALL, isNotificationCall);
        HoroScopeFragment fragment = new HoroScopeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNotificationCall = getArguments().getBoolean(ARG_NOTIFICATION_CALL, false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            updateToolbarTitle("Prediction");
            showHideToolbar(true);
            showHideBackButton(true);
            showHideBuyCreditsButton(View.VISIBLE);
            onHoroscopeTutorialListener = (HoroscopePresenter.OnHoroscopeTutorialListener) context;
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        view = new HoroscopeView(getActivity());
        horoscopeModel = new HoroscopeModel((AppCompatActivity) getContext(), apiInterface);
        presenter = new HoroscopePresenter(view, horoscopeModel, preferenceManger, abDatabase);
        presenter.onCreate();
        presenter.setOnHoroscopeTutorialListener(this);
        if (isNotificationCall) {
            presenter.openUserForecast();
            isNotificationCall = false;
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hideBuyTopicTooltip();
        showHideBuyCreditsButton(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        setStartTimeMills(abDatabase, Config.FORECAST_ACTION);
    }

    @Override
    public void onPause() {
        super.onPause();
        setEndTimeMills(abDatabase, Config.FORECAST_ACTION);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onDrawerOpen() {
        if (view != null)
            view.hideTooltipMenu();
    }

    @Override
    public void onComplete() {
        if (onHoroscopeTutorialListener != null) {
            onHoroscopeTutorialListener.onComplete();
        }
    }
}
