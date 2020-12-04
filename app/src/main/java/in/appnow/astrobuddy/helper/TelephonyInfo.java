package in.appnow.astrobuddy.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;

import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 11:02, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public final class TelephonyInfo {
    private static final String TAG = TelephonyInfo.class.getSimpleName();
    private static TelephonyInfo telephonyInfo;
    private String deviceID;
    private String imsiSIM1;
    private String imsiSIM2;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;

    private static class GeminiMethodNotFoundException extends Exception {
        private static final long serialVersionUID = -996812356902545308L;

        GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }

    public String getImsiSIM1() {
        return this.imsiSIM1;
    }

    public String getImsiSIM2() {
        return this.imsiSIM2;
    }

    public boolean isSIM1Ready() {
        return this.isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return this.isSIM2Ready;
    }

    public boolean isDualSIM() {
        return this.imsiSIM2 != null;
    }


    public String getDeviceID() {
        return this.deviceID;
    }


    private TelephonyInfo() {
    }

    @SuppressLint({"HardwareIds"})
    public static TelephonyInfo getInstance(Context context) {
        boolean z = true;
        if (telephonyInfo == null) {
            telephonyInfo = new TelephonyInfo();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null)
                    telephonyInfo.imsiSIM1 = telephonyManager.getDeviceId();
                else
                    telephonyInfo.imsiSIM1 = "";
            } else {
                telephonyInfo.imsiSIM1 = "";
            }
            telephonyInfo.imsiSIM2 = null;
            try {
                telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
                telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
            } catch (GeminiMethodNotFoundException e) {
              //  e.printStackTrace();
                try {
                    telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
                    telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                } catch (GeminiMethodNotFoundException e1) {
                    //e1.printStackTrace();
                    try {
                        telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceIdDs", 0);
                        telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceIdDs", 1);
                    } catch (GeminiMethodNotFoundException e2) {
                        //e2.printStackTrace();
                        try {
                            telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 0);
                            telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 1);
                        } catch (GeminiMethodNotFoundException e3) {
                          //  e3.printStackTrace();
                        }
                    }
                }
            }

            if (telephonyManager.getSimState() != 5) {
                z = false;
            }
            telephonyInfo.isSIM1Ready = z;
            telephonyInfo.isSIM2Ready = false;
            try {
                telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
                telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
            } catch (GeminiMethodNotFoundException e4) {
                //e4.printStackTrace();
                try {
                    telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
                    telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
                } catch (GeminiMethodNotFoundException e12) {
                   // e12.printStackTrace();
                }
            }
        }
        //telephonyInfo.deviceID =
        telephonyInfo.deviceID = setDeviceID(context);
        return telephonyInfo;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        String imsi = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Object ob_phone = Class.forName(telephony.getClass().getName()).getMethod(predictedMethodName, new Class[]{Integer.TYPE}).invoke(telephony, new Object[]{Integer.valueOf(slotID)});
            if (ob_phone != null) {
                imsi = ob_phone.toString();
            }
            Logger.ErrorLog(TAG, "Slot ID : " + slotID + " - Method Name : " + predictedMethodName + " - Device ID : " + imsi);
            return imsi;
        } catch (Exception e) {
           // e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Object ob_phone = Class.forName(telephony.getClass().getName()).getMethod(predictedMethodName, new Class[]{Integer.TYPE}).invoke(telephony, new Object[]{Integer.valueOf(slotID)});
            if (ob_phone == null || Integer.parseInt(ob_phone.toString()) != 5) {
                return false;
            }
            return true;
        } catch (Exception e) {
           // e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
    }

    @SuppressLint({"HardwareIds"})
    private static String setDeviceID(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null)
                return manager.getDeviceId();
            else return "";
        }
        return "";
    }

    public static void printTelephonyManagerMethodNamesForThisDevice(Context context) {
        try {
            Method[] methods = Class.forName(((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getClass().getName()).getMethods();
            for (int idx = 0; idx < methods.length; idx++) {
                Logger.ErrorLog(TAG, "\n" + methods[idx] + " declared by " + methods[idx].getDeclaringClass());
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
    }
}
