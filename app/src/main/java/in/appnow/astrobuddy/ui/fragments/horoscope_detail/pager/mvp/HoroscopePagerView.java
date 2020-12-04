package in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.TabPagerAdapter;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.models.SunSignModel;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.HoroscopeDetailFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.HoroscopeDetailPagerFragment;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 22:31, 26/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopePagerView extends BaseViewClass {

    private static final String TAG = HoroscopePagerView.class.getSimpleName();
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.background_image)
    ImageView background_image;

    private TabPagerAdapter adapter;

    private Context context;

    public HoroscopePagerView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.view_pager_layout, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, background_image);
    }

    public void setUpPager(HoroscopeDetailPagerFragment horoscopeDetailPagerFragment,int position) {
        adapter = new TabPagerAdapter(horoscopeDetailPagerFragment.getChildFragmentManager());
        List<SunSignModel> sunSignModelList = SunSignModel.getSunSignList(context);
        for (SunSignModel sunSignModel : sunSignModelList) {
            adapter.addFragment(HoroscopeDetailFragment.newInstance(sunSignModel));
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(sunSignModelList.size());

         updateViewPager(position);
    }

    public void updateViewPager(int position) {
        viewPager.setCurrentItem(position, true);
    }
}
