package in.appnow.astrobuddy.ui.fragments.myth_buster;

import android.content.Context;
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
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.MythBusterModel;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.MythBusterPresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.view.MythBusterView;

/**
 * Created by sonu on 11/04/2018
 * Copyright © 2018 sonu. All rights reserved.
 */
public class MythBusterFragment extends BaseFragment {

    // @Inject
    MythBusterView view;
    // @Inject
    MythBusterPresenter presenter;
    MythBusterModel model;

    @Inject
    APIInterface apiInterface;
    @Inject
    PreferenceManger preferenceManger;
    @Inject
    ABDatabase abDatabase;

    private Context context;

    private int mythId = 0;
    private boolean isVideo = false;
    private static final String ARG_MYTH_ID = "myth_id";
    private static final String ARG_MYTH_VIDEO = "myth_video";

    public MythBusterFragment() {
    }

    public static MythBusterFragment newInstance(int mythId, boolean isMythVideo) {

        Bundle args = new Bundle();
        args.putInt(ARG_MYTH_ID, mythId);
        args.putBoolean(ARG_MYTH_VIDEO, isMythVideo);
        MythBusterFragment fragment = new MythBusterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            updateToolbarTitle(context.getResources().getString(R.string.action_myth_buster));
            showHideToolbar(true);
            showHideBackButton(true);
            hideBottomBar(false);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mythId = getArguments().getInt(ARG_MYTH_ID, 0);
            isVideo = getArguments().getBoolean(ARG_MYTH_VIDEO, false);

            if (isVideo) {
                updateToolbarTitle("Videos");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        view = new MythBusterView(getActivity());
        model = new MythBusterModel((AppCompatActivity) getContext(), apiInterface);
        presenter = new MythBusterPresenter(view, model, preferenceManger, abDatabase);
        presenter.setMythId(mythId);
        presenter.setMythType(isVideo);
        view.setMythTypeVideo(isVideo);
        presenter.onCreate();
        // checkIfBackPromptIsShown();
        return view;
    }

    private void checkIfBackPromptIsShown() {
        if (presenter != null) {
            PreferenceManger preferenceManger = presenter.getPreferenceManger();
            if (preferenceManger != null) {
                if (preferenceManger.getBooleanValue(PreferenceManger.MYTH_BUSTER_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.MYTH_BUSTER_BACK_PRESS_HINT)) {
                    showToolbarBackPrompt(R.drawable.ic_menu);
                    preferenceManger.putBoolean(PreferenceManger.MYTH_BUSTER_BACK_PRESS_HINT, true);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVideo) {
            setStartTimeMills(abDatabase,Config.MYTH_BUSTER_VIDEO_ACTION);
        } else {
            setStartTimeMills(abDatabase,Config.MYTH_BUSTER_ACTION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isVideo) {
            setEndTimeMills(abDatabase,Config.MYTH_BUSTER_VIDEO_ACTION);
        } else {
            setEndTimeMills(abDatabase,Config.MYTH_BUSTER_ACTION);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
