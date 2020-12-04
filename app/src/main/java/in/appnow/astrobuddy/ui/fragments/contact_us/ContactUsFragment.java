package in.appnow.astrobuddy.ui.fragments.contact_us;

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
import in.appnow.astrobuddy.ui.fragments.contact_us.mvp.ContactUsPresenter;
import in.appnow.astrobuddy.ui.fragments.contact_us.mvp.ContactUsView;

/**
 * Created by sonu on 18:13, 05/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ContactUsFragment extends BaseFragment {

    private Context context;

    @Inject
    ContactUsView view;
    @Inject
    ContactUsPresenter presenter;

    public static ContactUsFragment newInstance() {

        Bundle args = new Bundle();

        ContactUsFragment fragment = new ContactUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try{
            showHideToolbar(true);
            updateToolbarTitle(context.getResources().getString(R.string.action_contact_us));
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

