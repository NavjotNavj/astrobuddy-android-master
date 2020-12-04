package in.appnow.astrobuddy.ui.fragments.contact_us.mvp;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.VectorUtils;

/**
 * Created by sonu on 18:16, 05/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ContactUsView extends BaseViewClass {

    @BindView(R.id.contact_us_mail_link)
    TextView mailLabel;
    @BindView(R.id.contact_us_web_link)
    TextView webLabel;
    @BindView(R.id.contact_us_mobile_link)
    TextView mobileLabel;
    @BindView(R.id.contact_us_image)
    ImageView contactUsImage;

    @BindView(R.id.background_image)
    ImageView backgroundImage;

    public ContactUsView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.contact_us_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context,backgroundImage);
        updateIconToLabel(context);

    }

    private void updateIconToLabel(Context context) {
        ImageUtils.setDrawableImage(context,contactUsImage,R.drawable.customer_service_icon);
        VectorUtils.setVectorCompoundDrawable(mailLabel, context, R.drawable.ic_email_black_24dp, 0, 0, 0, R.color.gunmetal);
        VectorUtils.setVectorCompoundDrawable(webLabel, context, R.drawable.ic_public_black_24dp, 0, 0, 0, R.color.gunmetal);
        VectorUtils.setVectorCompoundDrawable(mobileLabel, context, R.drawable.ic_phone_android_black_24dp, 0, 0, 0, R.color.gunmetal);

    }

}
