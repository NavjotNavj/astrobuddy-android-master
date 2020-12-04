package in.appnow.astrobuddy.user_authentication_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.PromoCodeModel;
import in.appnow.astrobuddy.user_authentication_module.activity.RegistrationActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_register.RegisterComponent;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_two.RegisterFragmentTwoPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_two.RegisterFragmentTwoView;
import in.appnow.astrobuddy.user_authentication_module.interfaces.OnValidationListener;
import in.appnow.astrobuddy.utils.Logger;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterFragmentStepTwo extends Fragment implements OnValidationListener {

    @Inject
    public RegisterFragmentTwoView registerFragmentTwoView;
    @Inject
    RegisterFragmentTwoPresenter registerFragmentTwoPresenter;
    private RegisterComponent registerComponent;

    public static final int APPLY_PROMOCODE_REQUEST_CODE = 1;

    public static RegisterFragmentStepTwo newInstance(Bundle bundle) {
        RegisterFragmentStepTwo fragment = new RegisterFragmentStepTwo();
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
        registerFragmentTwoPresenter.onCreate();
        Bundle bundle = getArguments();
        if (bundle != null) {
            registerFragmentTwoView.email_edittext.setText(bundle.getString(AstroAppConstants.EMAIL));
        }
        return registerFragmentTwoView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerFragmentTwoPresenter.onDestroy();
    }

    @Override
    public boolean checkValidation() {
        return registerFragmentTwoView.validateData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case APPLY_PROMOCODE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String promoCode = data.getStringExtra(PromoCodeModel.EXTRA_PROMO_CODE);
                    ApplyPromoCodeResponse response = data.getParcelableExtra(PromoCodeModel.EXTRA_PROMO_DATA);
                    int promoId = data.getIntExtra(PromoCodeModel.EXTRA_PROMO_ID,0);
                    registerFragmentTwoPresenter.promoCodeApplied(promoCode, response,promoId);
                }
                break;
        }
    }
}
