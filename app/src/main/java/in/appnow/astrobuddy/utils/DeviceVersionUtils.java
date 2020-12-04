package in.appnow.astrobuddy.utils;

import android.os.Build;

/**
 * Created by sonu on 11:02, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class DeviceVersionUtils {
    public static boolean isLollipopPlusOS() {
        return Build.VERSION.SDK_INT >= 22;
    }

    public static void getDeviceInfo() {
        Logger.ErrorLog("TAG", "SERIAL: " + Build.SERIAL);
        Logger.ErrorLog("TAG", "MODEL: " + Build.MODEL);
        Logger.ErrorLog("TAG", "ID: " + Build.ID);
        Logger.ErrorLog("TAG", "Manufacture: " + Build.MANUFACTURER);
        Logger.ErrorLog("TAG", "Version Code: " + Build.VERSION.RELEASE);
        Logger.ErrorLog("TAG", "SDK  " + Build.VERSION.SDK_INT);
        Logger.ErrorLog("TAG", "BRAND " + Build.BRAND);
    }
}
