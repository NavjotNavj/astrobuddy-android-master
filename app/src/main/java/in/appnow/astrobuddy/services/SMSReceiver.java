package in.appnow.astrobuddy.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.StringUtils;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = SMSReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String messageBody;
        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj != null) {
                SmsMessage[] messages = new SmsMessage[pdusObj.length];

                // getting SMS information from Pdu.
                for (int i = 0; i < pdusObj.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }

                for (SmsMessage currentMessage : messages) {
                    String address = currentMessage.getDisplayOriginatingAddress();
                    messageBody = currentMessage.getDisplayMessageBody();
                    Logger.ErrorLog(TAG, "OTP messageBody : " + messageBody);
                    String otp = StringUtils.extractNumberFromString(messageBody);
                    Logger.ErrorLog(TAG, "OTP received : " + otp);
                    if (otp.length() == 4) {
                        Logger.DebugLog(TAG, "OTP received and valid");
                        Intent myIntent = new Intent(context.getResources().getString(R.string.otp_received_action));
                        myIntent.putExtra("otp",otp);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        // Show Alert


                    } else
                        Logger.DebugLog(TAG, "OTP received but not valid");
                }
            }
        }
    }
}
