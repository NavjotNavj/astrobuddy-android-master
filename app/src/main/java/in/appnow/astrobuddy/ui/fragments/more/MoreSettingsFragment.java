package in.appnow.astrobuddy.ui.fragments.more;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.more.mvp.MoreSettingsFragmentPresenter;
import in.appnow.astrobuddy.ui.fragments.more.mvp.MoreSettingsFragmentView;

/**
 * Created by Abhishek Thanvi on 22/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class MoreSettingsFragment extends BaseFragment {

    @Inject
    MoreSettingsFragmentView view;
    @Inject
    MoreSettingsFragmentPresenter presenter;

    private Context context;

    public MoreSettingsFragment() {
    }

    public static MoreSettingsFragment newInstance() {

        Bundle args = new Bundle();
        MoreSettingsFragment fragment = new MoreSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            updateToolbarTitle("Settings");
            showHideBackButton(false);
            showHideToolbar(true);
        } catch (Exception ignored) {

        }
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
