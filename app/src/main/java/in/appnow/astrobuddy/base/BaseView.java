package in.appnow.astrobuddy.base;

/**
 * Created by sonu on 11:55, 07/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public interface BaseView {
    void onUnknownError(String error);

    void onTimeout();

    void onNetworkError();

    boolean isNetworkConnected();

    void onConnectionError();
}