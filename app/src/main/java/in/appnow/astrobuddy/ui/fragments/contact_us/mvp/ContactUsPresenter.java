package in.appnow.astrobuddy.ui.fragments.contact_us.mvp;

import in.appnow.astrobuddy.base.BasePresenter;

/**
 * Created by sonu on 18:16, 05/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ContactUsPresenter implements BasePresenter {

    private final ContactUsView view;
    private final ContactUsModel model;

    public ContactUsPresenter(ContactUsView view, ContactUsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void unAuthorizeUserAccess(String message) {

    }
}
