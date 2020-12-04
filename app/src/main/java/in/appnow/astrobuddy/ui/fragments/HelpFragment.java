package in.appnow.astrobuddy.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.response.AccountHelpResponse;
import in.appnow.astrobuddy.ui.common_activity.CommonActivity;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.VISIBLE;

/**
 * Created by sonu on 15:58, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HelpFragment extends BaseFragment implements BaseView {

    private Unbinder unbinder;

    @BindView(R.id.account_help_description_label)
    TextView descriptionLabel;
    @BindView(R.id.my_account_help_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.background_image)
    ImageView backgroundImage;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    @Inject
    APIInterface apiInterface;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Context context;

    public static HelpFragment newInstance() {

        Bundle args = new Bundle();

        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((CommonActivity) getActivity()).getComponent().inject(this);
        return inflater.inflate(R.layout.my_account_help_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        compositeDisposable.add(getSecurityTip());
    }

    public void showHideProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
        if (visibility == VISIBLE) {
            descriptionLabel.setVisibility(View.GONE);
        }
    }

    public void updateView(AccountHelpResponse data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionLabel.setText(Html.fromHtml(data.getAccountHelpInfo(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descriptionLabel.setText(Html.fromHtml(data.getAccountHelpInfo()));
        }
        descriptionLabel.setVisibility(VISIBLE);
    }

    private Disposable getSecurityTip() {
        showHideProgressBar(View.VISIBLE);
        return apiInterface.getChatSecurityTip()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> showHideProgressBar(View.GONE))
                .subscribeWith(new CallbackWrapper<AccountHelpResponse>(this) {
                    @Override
                    protected void onSuccess(AccountHelpResponse accountHelpResponse) {
                        if (accountHelpResponse != null) {
                            updateView(accountHelpResponse);
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
        unbinder.unbind();
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
