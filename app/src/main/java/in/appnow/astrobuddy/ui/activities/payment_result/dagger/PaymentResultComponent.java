package in.appnow.astrobuddy.ui.activities.payment_result.dagger;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.activities.payment_result.PaymentResultActivity;

/**
 * Created by sonu on 17:26, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@PaymentResultScope
@Component(modules = PaymentResultModule.class, dependencies = AppComponent.class)
public interface PaymentResultComponent {
    void inject(PaymentResultActivity paymentResultActivity);
}
