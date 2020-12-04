package in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.rest.APIInterface;

/**
 * Created by sonu on 22:31, 26/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopePagerModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public HoroscopePagerModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }
}
