package in.appnow.astrobuddy.ui.activities.promo_code.mvp.view;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.PromoCodeResponse;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 15:04, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodeView extends BaseViewClass implements BaseView {
    private static final String TAG = PromoCodeView.class.getSimpleName();
    @BindView(R.id.promo_edittext)
    AppCompatEditText enterPromoCode;
    @BindView(R.id.apply_button)
    TextView applyPromoCodeButton;
    @BindView(R.id.promo_code_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.promo_code_progress_bar)
    ProgressBar promoCodeProgressBar;
    @BindView(R.id.empty_promo_code_label)
    TextView emptyPromoCodeLabel;
    @BindView(R.id.promo_code_toolbar)
    Toolbar toolbar;
    @BindString(R.string.unknown_error)
    String unknownErrorString;

    public final PromoCodeAdapter adapter = new PromoCodeAdapter();

    public PromoCodeView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        inflate(appCompatActivity, R.layout.activity_promo_code, this);
        ButterKnife.bind(this);
        setUpRecyclerView();
        toolbar.setNavigationOnClickListener(view -> appCompatActivity.finish());
    }

    public Observable<Object> observeApplyButtonClick() {
        return RxView.clicks(applyPromoCodeButton);
    }


    public void showHideProgressBar(int visibility) {
        promoCodeProgressBar.setVisibility(visibility);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }


    public void updateView(PromoCodeResponse response) {
        if (response.isErrorStatus()) {
            showHideView(true, response.getErrorMessage());
        } else if (response.getPromoCodeDetails() != null && response.getPromoCodeDetails().getPromoCount() > 0 && response.getPromoCodeDetails().getPromoCodes() != null && response.getPromoCodeDetails().getPromoCodes().size() > 0) {
            showHideView(false, "");
            adapter.swapData(response.getPromoCodeDetails().getPromoCodes());
        } else {
            showHideView(true, "No Plans.");
        }
    }

    public void showHideView(boolean isEmpty, String message) {
        if (isEmpty) {
            emptyPromoCodeLabel.setText(message);
            emptyPromoCodeLabel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyPromoCodeLabel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public String getPromoCode() {
        return enterPromoCode.getText().toString().trim();
    }

    @Override
    public void onUnknownError(String error) {
        ToastUtils.shortToast(error);
    }

    @Override
    public void onTimeout() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        ToastUtils.shortToast(unknownErrorString);
    }


}
