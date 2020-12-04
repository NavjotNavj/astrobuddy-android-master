package in.appnow.astrobuddy.interfaces;

/**
 * Created by sonu on 11:36, 05/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public interface OnPaymentListener {

    public void onPaymentCompleted(String paymentId);

    public void onPaymentFailed(int code,String response);
}
