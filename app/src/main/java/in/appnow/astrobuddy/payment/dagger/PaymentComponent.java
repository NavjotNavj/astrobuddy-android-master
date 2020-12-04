package in.appnow.astrobuddy.payment.dagger;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.payment.PaymentWebViewActivity;

/**
 * Created by sonu on 11:32, 22/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@PaymentScope
@Component(modules = PaymentModule.class, dependencies = AppComponent.class)
public interface PaymentComponent {
    void inject(PaymentWebViewActivity paymentWebViewActivity);
}
