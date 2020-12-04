package in.appnow.astrobuddy.ui.fragments.horoscope_detail.mvp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Map;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.custom_views.SmartTextView;
import in.appnow.astrobuddy.models.SunSignModel;
import in.appnow.astrobuddy.rest.response.HoroscopeDetailResponse;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.relex.circleindicator.CircleIndicator;

import static in.appnow.astrobuddy.app.AstroAppConstants.TODAY;

/**
 * Created by Abhishek Thanvi on 24/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class HoroScopeDetailView extends BaseViewClass implements BaseView {

    private static final String TAG = HoroScopeDetailView.class.getSimpleName();
    Context context;
    @BindView(R.id.viewPager)
    VerticalViewPager verticalViewPager;
    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.horoscope_name)
    AppCompatTextView horoscope_name;
    @BindView(R.id.horoscope_icon)
    ImageView horoscope_icon;
    @BindView(R.id.horoscope_detail_label)
    SmartTextView horoscopeDetailLabel;
    @BindView(R.id.horoscope_detail_scrollview)
    NestedScrollView nestedScrollView;
    @BindView(R.id.horoscope_detail_progress)
    ProgressBar progressBar;
    @BindView(R.id.today_detail)
    TextView todayDetail;
    @BindView(R.id.tomorrow_detail)
    TextView tomorrowDetail;
    @BindView(R.id.monthly_detail)
    TextView monthlyDetail;
    @BindView(R.id.yearly_detail)
    TextView yearlyDetail;
    @BindView(R.id.horoscope_detail_chat_button)
    FloatingActionButton chatButton;
    @BindArray(R.array.horoscope_types)
    String[] horoscopeTypeArray;
    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private HoroscopeDetailResponse horoscopeDetailResponse;

    public HoroScopeDetailView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.fragment_horosocpe_detail, this);
        ButterKnife.bind(this, this);
        initViews();
    }

    public Observable<Object> observeChatButton() {
        return RxView.clicks(chatButton);
    }

    public void updateViews(SunSignModel sunSignModel) {
        //-1 for 0 position
        updateHoroscopeForecast(TODAY);
        showHideProgress(true);

        if (sunSignModel != null) {
            ImageUtils.setDrawableImage(context, horoscope_icon, sunSignModel.getSunIcon());
            SpannableString ss = new SpannableString(sunSignModel.getSunSignName().toUpperCase() + "   " + sunSignModel.getRomanName().toUpperCase());
            Drawable d = context.getResources().getDrawable(R.drawable.sword);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, sunSignModel.getSunSignName().toUpperCase().length() + 1, sunSignModel.getSunSignName().toUpperCase().length() + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            horoscope_name.setText(ss);
            //horoscope_name.setText(sunSignModel.getSunSignName().toUpperCase() + " / " + sunSignModel.getRomanName().toUpperCase());
        }
    }


    public void setHoroscopeDetailResponse(HoroscopeDetailResponse horoscopeDetailResponse) {
        this.horoscopeDetailResponse = horoscopeDetailResponse;
        showHideProgress(false);
        updateHoroscopeForecast(TODAY);
    }

    public Observable<Object> todayDetailClick() {
        return RxView.clicks(todayDetail);
    }

    public Observable<Object> tomorrowDetailClick() {
        return RxView.clicks(tomorrowDetail);
    }

    public Observable<Object> monthlyDetailClick() {
        return RxView.clicks(monthlyDetail);
    }

    public Observable<Object> yearlyDetailClick() {
        return RxView.clicks(yearlyDetail);
    }

    public void initViews() {

       /* Collections.addAll(horoscopeTypes, horoscopeTypeArray);

        HoroscopeDetailPagerAdapter horoscopeDetailPagerAdapter = new HoroscopeDetailPagerAdapter(context, horoscopeTypes);
        verticalViewPager.setAdapter(horoscopeDetailPagerAdapter);
        horoscopeDetailPagerAdapter.setOnPagerTextClickListener(this);
        circleIndicator.setViewPager(verticalViewPager);

        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }


   /* @Override
    public void onClick(int position) {
        if (position == 3) {
            verticalViewPager.setCurrentItem(0, true);
            updateHoroscopeForecast(1);
        } else {
            verticalViewPager.setCurrentItem(position + 1, true);
            updateHoroscopeForecast(position + 2);
        }
    }*/


    public void updateHoroscopeForecast(int forecastTypeId) {
        switch (forecastTypeId) {
            case 1:
                todayDetail.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.grey_bg_curve, R.color.white));
                todayDetail.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                tomorrowDetail.setBackground(null);
                tomorrowDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                monthlyDetail.setBackground(null);
                monthlyDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                yearlyDetail.setBackground(null);
                yearlyDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                break;
            case 2:
                tomorrowDetail.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.grey_bg_curve, R.color.white));
                tomorrowDetail.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                todayDetail.setBackground(null);
                todayDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                monthlyDetail.setBackground(null);
                monthlyDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                yearlyDetail.setBackground(null);
                yearlyDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                break;
            case 3:
                monthlyDetail.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.grey_bg_curve, R.color.white));
                monthlyDetail.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                todayDetail.setBackground(null);
                todayDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                tomorrowDetail.setBackground(null);
                tomorrowDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                yearlyDetail.setBackground(null);
                yearlyDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                break;
            case 4:
                yearlyDetail.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.grey_bg_curve, R.color.white));
                yearlyDetail.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                todayDetail.setBackground(null);
                todayDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                tomorrowDetail.setBackground(null);
                tomorrowDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                monthlyDetail.setBackground(null);
                monthlyDetail.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                break;
        }

        if (horoscopeDetailResponse != null) {
            for (Map.Entry<String, String> pair : horoscopeDetailResponse.horoscopeDetailHashMap.entrySet()) {
                if (pair.getKey().equalsIgnoreCase(String.valueOf(forecastTypeId))) {
                    if (pair.getValue() != null) {
                        horoscopeDetailLabel.setText(pair.getValue());
                        nestedScrollView.scrollTo(0, 0);
                    } else {
                        horoscopeDetailLabel.setText("Nothing to show.");
                    }
                    // TextJustification.justify(horoscopeDetailLabel);

                    return;
                }
            }
        }

    }

    public void showHideProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            horoscopeDetailLabel.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            horoscopeDetailLabel.setVisibility(View.VISIBLE);
        }
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


