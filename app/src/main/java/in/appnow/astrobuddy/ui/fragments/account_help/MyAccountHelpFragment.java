package in.appnow.astrobuddy.ui.fragments.account_help;

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
import in.appnow.astrobuddy.ui.fragments.account_help.mvp.AccountHelpPresenter;
import in.appnow.astrobuddy.ui.fragments.account_help.mvp.AccountHelpView;

/**
 * Created by sonu on 16:46, 17/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MyAccountHelpFragment extends BaseFragment {

    @Inject
    AccountHelpView view;
    @Inject
    AccountHelpPresenter presenter;

    private String helpType;

    public static MyAccountHelpFragment newInstance() {

        Bundle args = new Bundle();
        MyAccountHelpFragment fragment = new MyAccountHelpFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showHideBackButton(true);
            updateToolbarTitle(context.getResources().getString(R.string.information));
            showHideToolbar(true);
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
    public void onDetach() {
        //showHideBackButton(false);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
