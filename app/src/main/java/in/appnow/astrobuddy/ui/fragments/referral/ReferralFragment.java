package in.appnow.astrobuddy.ui.fragments.referral;

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
import in.appnow.astrobuddy.ui.fragments.referral.mvp.ReferralPresenter;
import in.appnow.astrobuddy.ui.fragments.referral.mvp.view.ReferralView;

/**
 * Created by sonu on 11/04/2018
 * Copyright © 2018 sonu. All rights reserved.
 */
public class ReferralFragment extends BaseFragment {

    @Inject
    ReferralView view;
    @Inject
    ReferralPresenter presenter;

    public ReferralFragment() {
    }

    public static ReferralFragment newInstance() {

        Bundle args = new Bundle();

        ReferralFragment fragment = new ReferralFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
        updateToolbarTitle(context.getResources().getString(R.string.action_referral));
            showHideToolbar(true);
            showHideBackButton(true);
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
