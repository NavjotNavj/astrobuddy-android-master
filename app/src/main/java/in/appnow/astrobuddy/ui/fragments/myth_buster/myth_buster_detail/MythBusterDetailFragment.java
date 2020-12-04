package in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail;

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
import in.appnow.astrobuddy.rest.response.MythBuster;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailPresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailView;

/**
 * Created by sonu on 16:44, 02/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterDetailFragment extends BaseFragment {

    @Inject
    MythBusterDetailView view;
    @Inject
    MythBusterDetailPresenter presenter;

    private static final String ARG_MYTH_DETAIL = "myth_detail";

    private MythBuster mythBuster;

    public static MythBusterDetailFragment newInstance(MythBuster mythBuster) {

        Bundle args = new Bundle();
        try {
            args.putParcelable(ARG_MYTH_DETAIL, mythBuster);
        } catch (Exception ignored) {

        }
        MythBusterDetailFragment fragment = new MythBusterDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            showHideBackButton(true);
            showHideToolbar(true);
            updateToolbarTitle(context.getResources().getString(R.string.action_myth_buster));
            hideBottomBar(true);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mythBuster = getArguments().getParcelable(ARG_MYTH_DETAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        if (mythBuster != null)
            presenter.setUpMythBuster(mythBuster);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
