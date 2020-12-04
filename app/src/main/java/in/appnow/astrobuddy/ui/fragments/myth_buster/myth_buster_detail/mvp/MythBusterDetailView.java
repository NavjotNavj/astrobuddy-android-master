package in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.MythBuster;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 16:45, 02/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterDetailView extends BaseViewClass implements BaseView {

    @BindView(R.id.myth_buster_detail_image_view)
    ImageView imageView;
    @BindView(R.id.myth_buster_detail_title_label)
    TextView titleLabel;
    @BindView(R.id.myth_buster_detail_content_label)
    TextView contentLabel;
    @BindView(R.id.myth_buster_detail_publish_date_label)
    TextView publishDateLabel;
    @BindView(R.id.myth_buster_share_button)
    Button shareButton;
    @BindView(R.id.myth_buster_start_chat_button)
    Button startChatButton;
    @BindString(R.string.published_on_format)
    String publishedOnFormat;
    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private MythBuster mythBuster;

    public MythBusterDetailView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.myth_buster_detail_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
    }

    public void updateViews(MythBuster mythBuster) {
        this.mythBuster = mythBuster;
        ImageUtils.loadImageUrl(getContext(), imageView, R.drawable.ic_logo_with_name, mythBuster.getSource());
        titleLabel.setText(mythBuster.getTitle());
        contentLabel.setText(mythBuster.getDescription());
        publishDateLabel.setText(String.format(publishedOnFormat, DateUtils.parseStringDate(mythBuster.getDate(), DateUtils.SERVER_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT)));
    }

    public Observable<Object> observeShareButton() {
        return RxView.clicks(shareButton);
    }

    public Observable<Object> observeStartChatButton() {
        return RxView.clicks(startChatButton);
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
