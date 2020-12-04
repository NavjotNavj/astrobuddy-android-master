package in.appnow.astrobuddy.user_authentication_module.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.user_authentication_module.activity.RegistrationActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register.RegisterComponent;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one.RegisterFragmentOnePresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one.RegisterFragmentOneView;
import in.appnow.astrobuddy.user_authentication_module.interfaces.OnValidationListener;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterFragmentStepOne extends Fragment implements OnValidationListener {

    private static final String TAG = RegisterFragmentStepOne.class.getSimpleName();
    @Inject
    public RegisterFragmentOneView registerFragmentOneView;
    @Inject
    RegisterFragmentOnePresenter registerFragmentOnePresenter;
    private RegisterComponent registerComponent;


    public static RegisterFragmentStepOne newInstance(Bundle bundle) {
        RegisterFragmentStepOne fragment = new RegisterFragmentStepOne();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        registerComponent = ((RegistrationActivity) getActivity()).getRegisterComponent();
        registerComponent.inject(this);
        registerFragmentOnePresenter.onCreate();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(AstroAppConstants.SOCIAL_ID)) {
            registerFragmentOneView.setPreData(bundle.getString(AstroAppConstants.SOCIAL_ID), bundle.getString(AstroAppConstants.SOCIAL_TYPE_ID)
                    , bundle.getString(AstroAppConstants.F_NAME), bundle.getString(AstroAppConstants.L_NAME), bundle.getString(AstroAppConstants.PHOTO));
        } else {
            Logger.ErrorLog("Bundle not found", "error");
        }

        return registerFragmentOneView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerFragmentOnePresenter.onDestroy();
    }

    @Override
    public boolean checkValidation() {
        return registerFragmentOneView != null && registerFragmentOneView.validateData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterFragmentOneView.REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                try {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    registerFragmentOneView.setPlace(place.getAddress(), place.getLatLng());
                } catch (Exception e) {
                    ToastUtils.longToast("Failed to pick location. Please try again.");
                }
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
