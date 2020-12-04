package in.appnow.astrobuddy.payment.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;

/**
 * Created by sonu on 11:33, 22/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class PaymentModule {

    private final AppCompatActivity appCompatActivity;

    public PaymentModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }
}
