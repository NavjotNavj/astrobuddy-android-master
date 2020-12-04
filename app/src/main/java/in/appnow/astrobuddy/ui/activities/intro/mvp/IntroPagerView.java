package in.appnow.astrobuddy.ui.activities.intro.mvp;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.IntroPagerAdapter;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.ui.activities.intro.IntroModel;
import io.reactivex.Observable;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by sonu on 17:17, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class IntroPagerView extends BaseViewClass {
    @BindView(R.id.intro_pager)
    ViewPager introPager;
    @BindView(R.id.intro_circle_indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.skip_button)
    Button skipButton;
    @BindView(R.id.next_button)
    Button nextButton;

    private final IntroPagerAdapter adapter = new IntroPagerAdapter(getContext());

    public IntroPagerView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        inflate(appCompatActivity, R.layout.intro_activity, this);
        ButterKnife.bind(this);
        setUpViewPager();
    }

    private void setUpViewPager() {
        introPager.setAdapter(adapter);
        introPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onButtonTextChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public Observable<Object> observeSkipButton() {
        return RxView.clicks(skipButton);
    }

    public Observable<Object> observeNextButton() {
        return RxView.clicks(nextButton);
    }

    public void setUpIntroList(List<IntroModel> upIntroList) {
        adapter.swapData(upIntroList);
        introPager.setOffscreenPageLimit(adapter.getCount());
        circleIndicator.setViewPager(introPager);
    }


    public boolean onNextClick() {
        int currentItem = introPager.getCurrentItem();
        onButtonTextChange(currentItem);
        if (currentItem < adapter.getCount() - 1) {
            introPager.setCurrentItem(introPager.getCurrentItem() + 1, true);
            return false;
        }
        return true;
    }

    public void onButtonTextChange(int currentItem) {
        if (currentItem == adapter.getCount() - 1) {
            nextButton.setText("Get Started");
            skipButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setText("Next");
            skipButton.setVisibility(View.VISIBLE);
        }
    }
}
