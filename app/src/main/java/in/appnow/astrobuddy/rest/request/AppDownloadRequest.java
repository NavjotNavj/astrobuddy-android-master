package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 17:28, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AppDownloadRequest {
    @SerializedName("serial")
    private String serial;
    @SerializedName("model")
    private String modelNumber;
    @SerializedName("build_id")
    private String buildId;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("os_ver")
    private String deviceOSVersion;
    @SerializedName("os_api_ver")
    private int deviceAPILevel;
    @SerializedName("brand")
    private String brand;
    @SerializedName("imei1")
    private String imei1;
    @SerializedName("imei2")
    private String imei2;
    @SerializedName("imei_device_id")
    private String imeiDeviceId;
    @SerializedName("device_id")
    private String androidUniqueId;
    @SerializedName("random_uuid")
    private String randomUUID;
    @SerializedName("app_version_name")
    private String appVersionName;
    @SerializedName("app_version_code")
    private int appVersionCode;
    @SerializedName("fcm_token")
    private String fcmToken;
    @SerializedName("device_type")
    private String deviceType;
    @SerializedName("device_locale")
    private String locale;

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public void setDeviceAPILevel(int deviceAPILevel) {
        this.deviceAPILevel = deviceAPILevel;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImei1(String imei1) {
        this.imei1 = imei1;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public void setImeiDeviceId(String imeiDeviceId) {
        this.imeiDeviceId = imeiDeviceId;
    }

    public void setAndroidUniqueId(String androidUniqueId) {
        this.androidUniqueId = androidUniqueId;
    }

    public void setRandomUUID(String randomUUID) {
        this.randomUUID = randomUUID;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getSerial() {
        return serial;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getBuildId() {
        return buildId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public int getDeviceAPILevel() {
        return deviceAPILevel;
    }

    public String getBrand() {
        return brand;
    }

    public String getImei1() {
        return imei1;
    }

    public String getImei2() {
        return imei2;
    }

    public String getImeiDeviceId() {
        return imeiDeviceId;
    }

    public String getAndroidUniqueId() {
        return androidUniqueId;
    }

    public String getRandomUUID() {
        return randomUUID;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return "AppDownloadRequest{" +
                "serial='" + serial + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", buildId='" + buildId + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", deviceOSVersion='" + deviceOSVersion + '\'' +
                ", deviceAPILevel=" + deviceAPILevel +
                ", brand='" + brand + '\'' +
                ", imei1='" + imei1 + '\'' +
                ", imei2='" + imei2 + '\'' +
                ", imeiDeviceId='" + imeiDeviceId + '\'' +
                ", androidUniqueId='" + androidUniqueId + '\'' +
                ", randomUUID='" + randomUUID + '\'' +
                ", appVersionName='" + appVersionName + '\'' +
                ", appVersionCode=" + appVersionCode +
                ", fcmToken='" + fcmToken + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
