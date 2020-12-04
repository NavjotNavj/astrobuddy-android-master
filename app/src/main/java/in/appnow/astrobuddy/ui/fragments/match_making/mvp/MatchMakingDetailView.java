package in.appnow.astrobuddy.ui.fragments.match_making.mvp;

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
import in.appnow.astrobuddy.ui.fragments.match_making.MatchMakingDetailPagerFragment;
import io.reactivex.Observable;

/**
 * Created by sonu on 15:13, 11/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingDetailView extends BaseViewClass implements BaseView {

    @BindView(R.id.match_making_view_pager)
    ViewPager viewPager;
    @BindView(R.id.match_making_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.match_making_chat_button)
    FloatingActionButton chatButton;

    @BindArray(R.array.match_making_tab_array)
    String[] tabArray;

    public static final String ASHTAKOOT_URL = "match_ashtakoot_points";
    public static final String DASHAKOOT_URL = "match_dashakoot_points";

    public MatchMakingDetailView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.match_making_detail_fragment, this);
        ButterKnife.bind(this, this);
    }

    public Observable<Object> observeChatButton() {
        return RxView.clicks(chatButton);
    }

    public void setUpViewPager(FragmentManager fragmentManager, String dobMale, String tobMale, String latLngMale, String dobFemale, String tobFemale, String latLngFemale) {
        TabPagerAdapter adapter = new TabPagerAdapter(fragmentManager);
        adapter.addFragment(MatchMakingDetailPagerFragment.newInstance(ASHTAKOOT_URL, dobMale, tobMale, latLngMale, dobFemale, tobFemale, latLngFemale), tabArray[0]);
        adapter.addFragment(MatchMakingDetailPagerFragment.newInstance(DASHAKOOT_URL, dobMale, tobMale, latLngMale, dobFemale, tobFemale, latLngFemale), tabArray[1]);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
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
