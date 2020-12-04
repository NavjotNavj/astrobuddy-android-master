package in.appnow.astrobuddy.ui.fragments.myaccount;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.account_help.MyAccountHelpFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.MyAccountPresenter;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.view.MyAccountView;
import in.appnow.astrobuddy.utils.FragmentUtils;

/**
 * Created by sonu on 11/04/2018
 * Copyright © 2018 sonu. All rights reserved.
 */
public class MyAccountFragment extends BaseFragment {

    @Inject
    MyAccountView view;
    @Inject
    MyAccountPresenter presenter;

    private Context context;

    public MyAccountFragment() {
    }

    public static MyAccountFragment newInstance() {

        Bundle args = new Bundle();

        MyAccountFragment fragment = new MyAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            updateToolbarTitle(context.getResources().getString(R.string.action_my_account));
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
        setHasOptionsMenu(true);
        //checkIfBackPromptIsShown();
        return view;
    }

    private void checkIfBackPromptIsShown() {
        if (presenter != null) {
            PreferenceManger preferenceManger = presenter.getPreferenceManger();
            if (preferenceManger != null) {
                if (preferenceManger.getBooleanValue(PreferenceManger.MY_ACCOUNT_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.MY_ACCOUNT_BACK_PRESS_HINT)) {
                    showToolbarBackPrompt(R.drawable.ic_menu);
                    preferenceManger.putBoolean(PreferenceManger.MY_ACCOUNT_BACK_PRESS_HINT, true);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_account_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                if (AstroApplication.getInstance().isInternetConnected(true))
                    FragmentUtils.onChangeFragment(((AppCompatActivity) context).getSupportFragmentManager(), R.id.container_view, MyAccountHelpFragment.newInstance(), FragmentUtils.ACCOUNT_HELP_FRAGMENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationUtils.clearSingleNotification(Config.END_CHAT_NOTIFICATION_ID);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
