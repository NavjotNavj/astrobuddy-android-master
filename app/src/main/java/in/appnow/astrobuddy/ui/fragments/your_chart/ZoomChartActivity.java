package in.appnow.astrobuddy.ui.fragments.your_chart;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.custom_views.ZoomLayout;
import in.appnow.astrobuddy.horoscope_chart.ChartViewNorth;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by sonu on 12:22, 13/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ZoomChartActivity extends BaseActivity {

    private static final String ARG_RASHI_ARR = "rashi_arr";
    private static final String ARG_PLANET_ARR = "planet_arr";
    @BindView(R.id.chart_zoom_layout)
    ZoomLayout chartZoomLayout;
    @BindView(R.id.close_chart_activity)
    ImageView closeActivity;

    public static void startZoomChartActivity(Context context, int[] mCurrentRashiArr, String[] mCurrentPlanetStr) {
        Bundle bundle = new Bundle();
        bundle.putIntArray(ARG_RASHI_ARR, mCurrentRashiArr);
        bundle.putStringArray(ARG_PLANET_ARR, mCurrentPlanetStr);
        Intent intent = new Intent(context, ZoomChartActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_zoom);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(ARG_RASHI_ARR) && getIntent().hasExtra(ARG_PLANET_ARR)) {
            int[] mCurrentRashiArr = getIntent().getIntArrayExtra(ARG_RASHI_ARR);
            String[] mCurrentPlanetStr = getIntent().getStringArrayExtra(ARG_PLANET_ARR);
            drawChart(chartZoomLayout, mCurrentRashiArr, mCurrentPlanetStr);
        } else {
            finish();
        }
    }

    private void drawChart(FrameLayout chartLayout, int[] mCurrentRashiArr, String[] mCurrentPlanetStr) {
        Display display = getWindowManager().getDefaultDisplay();

        ChartViewNorth view = new ChartViewNorth(this, display.getWidth(), mCurrentRashiArr, mCurrentPlanetStr);
        view.setLayoutParams(new ActionBar.LayoutParams(display.getWidth(), display.getWidth()));
        chartLayout.addView(view);

    }


    @OnClick(R.id.close_chart_activity)
    void closeActivity() {
        finish();
    }
}
