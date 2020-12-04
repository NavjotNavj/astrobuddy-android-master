package in.appnow.astrobuddy.ui.fragments.match_making;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import javax.inject.Inject;

import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingPresenter;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingView;
import in.appnow.astrobuddy.utils.Logger;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by sonu on 12:25, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingFragment extends BaseFragment {

    private static final String TAG = MatchMakingFragment.class.getSimpleName();
    @Inject
    MatchMakingView view;
    @Inject
    MatchMakingPresenter presenter;

    public static MatchMakingFragment newInstance() {

        Bundle args = new Bundle();

        MatchMakingFragment fragment = new MatchMakingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showHideBackButton(true);
            updateToolbarTitle("Match Making");
            showHideToolbar(true);
            hideBottomBar(true);
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        showHideBackButton(true);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MatchMakingView.REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                view.setPlace(place.getAddress(), place.getLatLng());
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            // TODO: Handle the error.
            Logger.DebugLog(TAG, status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.

        }
    }
}
