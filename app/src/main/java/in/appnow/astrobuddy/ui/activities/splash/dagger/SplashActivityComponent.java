package in.appnow.astrobuddy.ui.activities.splash.dagger;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.activities.splash.SplashActivity;

/**
 * Created by sonu on 12:35, 19/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@SplashScope
@Component(modules = SplashActivityModule.class, dependencies = AppComponent.class)
public interface SplashActivityComponent {
    void inject(SplashActivity activity);
}
