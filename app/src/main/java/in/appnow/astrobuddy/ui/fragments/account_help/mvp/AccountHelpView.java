package in.appnow.astrobuddy.ui.fragments.account_help.mvp;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.AccountHelpResponse;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;

/**
 * Created by sonu on 16:17, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AccountHelpView extends BaseViewClass implements BaseView {

    private static final String TAG = AccountHelpView.class.getSimpleName();
    @BindView(R.id.account_help_description_label)
    TextView descriptionLabel;
    @BindView(R.id.my_account_help_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.background_image)
    ImageView backgroundImage;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private final Context context;

    public AccountHelpView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.my_account_help_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);

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
        descriptionLabel.setVisibility(View.VISIBLE);


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
