package in.appnow.astrobuddy.ui.activities.promo_code.dagger;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.activities.promo_code.PromoCodeActivity;

/**
 * Created by sonu on 15:04, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@PromoCodeActivityScope
@Component(modules = PromoCodeModule.class, dependencies = AppComponent.class)
public interface PromoCodeActivityComponent {

    void inject(PromoCodeActivity promoCodeActivity);
}
