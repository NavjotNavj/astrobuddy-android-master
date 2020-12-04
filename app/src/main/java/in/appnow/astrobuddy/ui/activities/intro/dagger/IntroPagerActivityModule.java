package in.appnow.astrobuddy.ui.activities.intro.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.intro.mvp.IntroPagerModel;
import in.appnow.astrobuddy.ui.activities.intro.mvp.IntroPagerPresenter;
import in.appnow.astrobuddy.ui.activities.intro.mvp.IntroPagerView;

/**
 * Created by sonu on 11:49, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class IntroPagerActivityModule {

    private final AppCompatActivity appCompatActivity;

    public IntroPagerActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    /* Intro Pager Activity MVP Injection */
    @Provides
    @IntroPagerActivityScope
    public IntroPagerView introPagerView() {
        return new IntroPagerView(appCompatActivity);
    }

    @Provides
    @IntroPagerActivityScope
    public IntroPagerModel introPagerModel() {
        return new IntroPagerModel(appCompatActivity);
    }

    @Provides
    @IntroPagerActivityScope
    public IntroPagerPresenter introPagerPresenter(IntroPagerView view, IntroPagerModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new IntroPagerPresenter(view, model, preferenceManger, abDatabase);
    }

}
