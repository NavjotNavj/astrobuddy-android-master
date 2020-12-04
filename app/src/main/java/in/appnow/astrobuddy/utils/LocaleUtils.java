package in.appnow.astrobuddy.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by sonu on 11/04/2018
 * Copyright © 2018 sonu. All rights reserved.
 */
public class LocaleUtils {
    public static Locale getDeviceLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    public static String fetchCountryISO(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return getDeviceLocale(context).getCountry().toUpperCase();
        String countryCodeValue = tm.getSimCountryIso();//previously getNetworkCountryIso()
        if (!TextUtils.isEmpty(countryCodeValue))
            return countryCodeValue.toUpperCase();
        else return getDeviceLocale(context).getCountry().toUpperCase();
    }
}
