package in.appnow.astrobuddy.ui.fragments.your_chart.mvp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.TabPagerAdapter;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.ui.fragments.your_chart.pager_items.HoroscopeBasicDetailFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.pager_items.HoroscopeDashaFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.pager_items.HoroscopePlanetryChartFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.pager_items.HoroscopePlanetryPositionsFragment;
import io.reactivex.Observable;

/**
 * Created by sonu on 17:33, 06/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeChartPagerView extends BaseViewClass implements BaseView {

    @BindView(R.id.chart_view_pager)
    ViewPager viewPager;
    @BindView(R.id.chart_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.chart_chat_button)
    FloatingActionButton chatButton;

    private Context context;

    @BindArray(R.array.horoscope_chart_tab_array)
    String[] horoscopeChartArray;


    public HoroscopeChartPagerView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.chart_pager_fragment, this);
        ButterKnife.bind(this, this);
    }

    public Observable<Object> observeChatButton() {
        return RxView.clicks(chatButton);
    }


    public void setUpViewPager(FragmentManager fragmentManager, String dob, String latLng) {
        TabPagerAdapter adapter = new TabPagerAdapter(fragmentManager);
        adapter.addFragment(HoroscopeBasicDetailFragment.newInstance(dob, latLng), horoscopeChartArray[0]);
        adapter.addFragment(HoroscopePlanetryChartFragment.newInstance(dob, latLng), horoscopeChartArray[1]);
        adapter.addFragment(HoroscopeDashaFragment.newInstance(dob, latLng), horoscopeChartArray[2]);
        adapter.addFragment(HoroscopePlanetryPositionsFragment.newInstance(dob, latLng), horoscopeChartArray[3]);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onUnknownError(String error) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {

    }


}
