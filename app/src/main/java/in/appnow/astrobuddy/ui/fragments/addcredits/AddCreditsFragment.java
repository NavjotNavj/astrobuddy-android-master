package in.appnow.astrobuddy.ui.fragments.addcredits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.OnPaymentListener;
import in.appnow.astrobuddy.payment.PaymentWebViewActivity;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.PromoCodeModel;
import in.appnow.astrobuddy.ui.fragments.addcredits.mvp.AddCreditsPresenter;
import in.appnow.astrobuddy.ui.fragments.addcredits.mvp.AddCreditsView;
import in.appnow.astrobuddy.utils.Logger;

import static android.app.Activity.RESULT_OK;
import static in.appnow.astrobuddy.ui.fragments.addcredits.mvp.AddCreditsModel.RAZOR_PAY_REQUEST_CODE;

/**
 * Created by sonu on 11/04/2018
 * Copyright © 2018 sonu. All rights reserved.
 */
public class AddCreditsFragment extends BaseFragment implements OnPaymentListener {

    private static final String ARG_TOPICS = "topic_count";
    private static final String TAG = AddCreditsFragment.class.getSimpleName();

    @Inject
    AddCreditsView view;
    @Inject
    AddCreditsPresenter presenter;
    @Inject
    ABDatabase abDatabase;

    public static final int APPLY_PROMOCODE_REQUEST_CODE = 1;
    public static final int CCAVENUE_PAYMENT_REQUEST_CODE = 2;

    private int getTopicsCount;

    public AddCreditsFragment() {
    }

    public static AddCreditsFragment newInstance(int topicsCount) {

        Bundle args = new Bundle();
        args.putInt(ARG_TOPICS, topicsCount);
        AddCreditsFragment fragment = new AddCreditsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showHideToolbar(true);
            updateToolbarTitle(context.getResources().getString(R.string.add_credits));
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getTopicsCount = getArguments().getInt(ARG_TOPICS, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        showHideBackButton(true);
        presenter.onCreate();
        presenter.setTopicsCount(getTopicsCount);
        //checkIfBackPromptIsShown();
        return view;
    }

    private void checkIfBackPromptIsShown() {
        if (presenter != null) {
            PreferenceManger preferenceManger = presenter.getPreferenceManger();
            if (preferenceManger != null) {
                if (preferenceManger.getBooleanValue(PreferenceManger.BUY_CREDITS_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.BUY_CREDITS_BACK_PRESS_HINT)) {
                    showToolbarBackPrompt(R.drawable.ic_action_navigation_white);
                    preferenceManger.putBoolean(PreferenceManger.BUY_CREDITS_BACK_PRESS_HINT, true);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setStartTimeMills(abDatabase, Config.ADD_TOPICS_ACTION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        setEndTimeMills(abDatabase, Config.ADD_TOPICS_ACTION);
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
            case APPLY_PROMOCODE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    ApplyPromoCodeResponse response = data.getParcelableExtra(PromoCodeModel.EXTRA_PROMO_DATA);
                    String promoCode = data.getStringExtra(PromoCodeModel.EXTRA_PROMO_CODE);
                    int promoId = data.getIntExtra(PromoCodeModel.EXTRA_PROMO_ID, 0);
                    presenter.promoCodeApplied(response, promoCode, promoId);
                }
                break;
            case CCAVENUE_PAYMENT_REQUEST_CODE:
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

            case RAZOR_PAY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    forceBackPress();
                }
                break;

        }
    }


    @Override
    public void onPaymentCompleted(String paymentId) {
        Logger.DebugLog(TAG, "Payment Id : " + paymentId);
        if (presenter != null)
            presenter.onPaymentDone(1, paymentId, "success");

    }

    @Override
    public void onPaymentFailed(int code, String response) {
        Logger.ErrorLog(TAG, "Payment Failed : " + response);
        if (presenter != null)
            presenter.onPaymentDone(code, "", response);

    }
}
