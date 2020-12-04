package in.appnow.astrobuddy.ui.fragments.upgrade_plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razorpay.PaymentData;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.payment.PaymentWebViewActivity;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.UpgradePlanPresenter;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.view.UpgradePlanView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sonu on 18:40, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpgradePlanFragment extends BaseFragment implements UpgradePlanPresenter.OnSubscriptionListener {

    private Context context;
    public static final int PAYMENT_REQUEST_CODE = 3;

    @Inject
    UpgradePlanView view;
    @Inject
    UpgradePlanPresenter presenter;


    public static UpgradePlanFragment newInstance() {

        Bundle args = new Bundle();
        UpgradePlanFragment fragment = new UpgradePlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            showHideBackButton(true);
            updateToolbarTitle(context.getResources().getString(R.string.change_plan));
            showHideToolbar(true);
        } catch (Exception ignored) {

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) context).getComponent().inject(this);
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
    public void onDetach() {
       // showHideBackButton(false);
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PAYMENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    forceBackPress();
                } else {
                    String errorMessage = "Unknown error occurred. Please try again. If your amount get deducted then please contact us.";
                    if (data != null && data.hasExtra(PaymentWebViewActivity.ARG_ERROR_MESSAGE)) {
                        errorMessage = data.getStringExtra(PaymentWebViewActivity.ARG_ERROR_MESSAGE);
                    }
                    DialogHelperClass.showMessageOKCancel(getContext(), errorMessage, "Ok", "Cancel", null, null);
                }
                break;

        }
    }

    @Override
    public void onSubscriptionSuccess(String paymentId, PaymentData paymentData) {
        if (presenter != null)
            presenter.onPaymentDone(1, paymentId, "success", paymentData);
    }

    @Override
    public void onSubscriptionFailed(int code, String response, PaymentData paymentData) {
        if (presenter != null)
            presenter.onPaymentDone(code, "", response, paymentData);
    }

}
