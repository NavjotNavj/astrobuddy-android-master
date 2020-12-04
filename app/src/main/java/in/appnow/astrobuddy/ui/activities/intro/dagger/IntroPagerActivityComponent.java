package in.appnow.astrobuddy.ui.activities.intro.dagger;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.activities.intro.IntroPagerActivity;

/**
 * Created by sonu on 11:48, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@IntroPagerActivityScope
@Component(modules = IntroPagerActivityModule.class, dependencies = AppComponent.class)
public interface IntroPagerActivityComponent {
    void inject(IntroPagerActivity introPagerActivity);

}
