package in.appnow.astrobuddy.user_authentication_module.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login.LoginComponent;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two.ForgotPasswordTwoPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_forgot_password_step_two.ForgotPasswordTwoView;


/**
 * Created by NILESH BHARODIYA on 10-09-2019.
 */
public class ForgotPasswordTwoFragment extends Fragment {

    public static final String ARG_NUMBER = "number";
    public static final String ARG_COUNTRY_CODE = "country_code";

    @Inject
    public ForgotPasswordTwoView view;

    @Inject
    public ForgotPasswordTwoPresenter presenter;

    private String number, countyCode;

    public static ForgotPasswordTwoFragment getInstance(String number, String countryCode) {
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, number);
        args.putString(ARG_COUNTRY_CODE, countryCode);

        ForgotPasswordTwoFragment fragment = new ForgotPasswordTwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number = getArguments().getString(ARG_NUMBER);
            countyCode = getArguments().getString(ARG_COUNTRY_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LoginComponent loginComponent = ((LoginActivity) getActivity()).getLoginComponent();
        loginComponent.inject(this);
        presenter.onCreate();
        presenter.fetchData(number, countyCode);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
