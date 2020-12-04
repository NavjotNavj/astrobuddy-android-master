package in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;

/**
 * Created by sonu on 22:31, 26/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopePagerPresenter implements BasePresenter {
    private final HoroscopePagerView view;
    private final HoroscopePagerModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private int position;

    public HoroscopePagerPresenter(HoroscopePagerView view, HoroscopePagerModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger =preferenceManger;
        this.abDatabase = abDatabase;
    }

    public void setPosition(int position) {
        this.position = position;
        view.updateViewPager(position);
    }

    @Override
    public void onCreate() {
        preferenceManger.putBoolean(PreferenceManger.FORECAST_SCREEN_SHOW, true);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void unAuthorizeUserAccess(String message) {

    }
}
