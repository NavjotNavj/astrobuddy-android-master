package in.appnow.astrobuddy.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import java.util.UUID;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.helper.TelephonyInfo;
import in.appnow.astrobuddy.rest.request.AppDownloadRequest;


/**
 * Created by sonu on 15:36, 18/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class DeviceUtils {
    private static final String TAG = DeviceUtils.class.getSimpleName();

    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        ToastUtils.longToast(R.string.camera_not_supported);
        return false;
    }

    public static int deviceWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) AstroApplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static AppDownloadRequest getDeviceDataForAppDownload(Context context) {
        AppDownloadRequest appDownloadRequest = new AppDownloadRequest();
        @SuppressLint("HardwareIds") String deviceSerial = Build.SERIAL;
        String model = Build.MODEL;
        String buildId = Build.ID;
        String manufacturer = Build.MANUFACTURER;
        String deviceOSVersion = Build.VERSION.RELEASE;
        int deviceAPILevel = Build.VERSION.SDK_INT;
        String brand = Build.BRAND;
        String imei1 = "";
        String imei2 = "";
        String uniqueDeviceId = "";
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);
                imei1 = telephonyInfo.getImsiSIM1();
                imei2 = telephonyInfo.getImsiSIM2();
                if (imei1 == null) {
                    imei1 = "";
                }
                if (imei2 == null) {
                    imei2 = "";
                }
                uniqueDeviceId = telephonyInfo.getDeviceID();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //will be always latest until and unless device is factory reset
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        appDownloadRequest.setSerial(deviceSerial);
        appDownloadRequest.setModelNumber(model);
        appDownloadRequest.setBuildId(buildId);
        appDownloadRequest.setManufacturer(manufacturer);
        appDownloadRequest.setDeviceOSVersion(deviceOSVersion);
        appDownloadRequest.setDeviceAPILevel(deviceAPILevel);
        appDownloadRequest.setBrand(brand);
        appDownloadRequest.setImei1(imei1);
        appDownloadRequest.setImei2(imei2);
        appDownloadRequest.setImeiDeviceId(uniqueDeviceId);
        appDownloadRequest.setAndroidUniqueId(android_id);
        appDownloadRequest.setLocale(LocaleUtils.fetchCountryISO(context));
        appDownloadRequest.setAppVersionCode(BuildConfig.VERSION_CODE);
        appDownloadRequest.setAppVersionName(BuildConfig.VERSION_NAME);
        appDownloadRequest.setDeviceType("1");

        return appDownloadRequest;
    }

    //unique identifier for a specific installation.
    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

}
