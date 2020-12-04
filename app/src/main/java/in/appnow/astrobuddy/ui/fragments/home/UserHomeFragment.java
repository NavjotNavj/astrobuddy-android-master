package in.appnow.astrobuddy.ui.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomeModel;
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomePresenter;
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomeView;
import in.appnow.astrobuddy.ui.fragments.horoscope.HoroScopeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopePresenter;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.Logger;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class UserHomeFragment extends BaseFragment {

    private static final String ARG_POSITION = "position";
    @Inject
    UserHomeView view;
    @Inject
    UserHomePresenter presenter;

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            showHideToolbar(false);
            showHideBackButton(false);
            hideBottomBar(false);
        } catch (Exception ignored) {

        }
    }

    public static UserHomeFragment newInstance() {
        Bundle args = new Bundle();
        UserHomeFragment fragment = new UserHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        view.fragmentManager = getActivity().getSupportFragmentManager();
        presenter.onCreate();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (view != null) {
            view.cancelAutoSwipeTimer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (view != null) {
            view.isWelcomeDialogShow();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
