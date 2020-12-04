package in.appnow.astrobuddy.base;

/**
 * Created by sonu on 11:01, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public interface BasePresenter {
    public void onCreate();
    public void onDestroy();
    void unAuthorizeUserAccess(String message);

}
