package in.appnow.astrobuddy.ui.fragments.changePassword;

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
import in.appnow.astrobuddy.interfaces.OnRemoveFragmentListener;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.changePassword.mvp.ChangePasswordPresenter;
import in.appnow.astrobuddy.ui.fragments.changePassword.mvp.ChangePasswordView;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;

/**
 * Created by sonu on 11/04/2018
 * Copyright © 2018 sonu. All rights reserved.
 */
public class ChangePasswordFragment extends BaseFragment implements OnRemoveFragmentListener {

    @Inject
    ChangePasswordView view;
    @Inject
    ChangePasswordPresenter presenter;

    public ChangePasswordFragment() {
    }

    public static ChangePasswordFragment newInstance() {

        Bundle args = new Bundle();

        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
        showHideBackButton(true);
        showHideToolbar(true);
        updateToolbarTitle(context.getResources().getString(R.string.change_password));
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        view.setOnRemoveFragmentListener(this);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onDetach() {
     //   showHideBackButton(false);
        super.onDetach();
    }

    @Override
    public void removeFragment() {
        FragmentUtils.onChangeFragment(getActivity().getSupportFragmentManager(), R.id.container_view, MyProfileFragment.newInstance(), FragmentUtils.MY_PROFILE_FRAGMENT);
    }
}
