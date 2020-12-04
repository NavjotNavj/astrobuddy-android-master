package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequestModel {

    @SerializedName("password")
    private String password;

    @SerializedName("login_action")
    private String loginAction;

    @SerializedName("fcm_token")
    private String fcmToken;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("id")
    private String id;

    @SerializedName("country_code")
    private String countryCode;


    /* Device details */
    @SerializedName("serial")
    private String serial;
    @SerializedName("imei1")
    private String imei1;
    @SerializedName("imei2")
    private String imei2;
    @SerializedName("imei_device_id")
    private String imeiDeviceId;
    @SerializedName("device_id")
    private String deviceId;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setLoginAction(String loginAction) {
        this.loginAction = loginAction;
    }

    public String getLoginAction() {
        return loginAction;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getId() {
        return id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getImei1() {
        return imei1;
    }

    public void setImei1(String imei1) {
        this.imei1 = imei1;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getImeiDeviceId() {
        return imeiDeviceId;
    }

    public void setImeiDeviceId(String imeiDeviceId) {
        this.imeiDeviceId = imeiDeviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return
                "LoginRequestModel{" +
                        "password = '" + password + '\'' +
                        ",login_action = '" + loginAction + '\'' +
                        ",fcm_token = '" + fcmToken + '\'' +
                        ",device_type = '" + deviceType + '\'' +
                        ",id = '" + id + '\'' +
                        ",countryCode = '" + countryCode + '\'' +
                        "}";
    }
}