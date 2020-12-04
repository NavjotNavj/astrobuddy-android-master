package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegistrationRequestModel implements Serializable {

    @SerializedName("fname")
    private String fname;

    @SerializedName("gender")
    private String gender;

    @SerializedName("social_registration_status")
    private int socialRegistrationStatus;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("otp")
    private String otp;

    @SerializedName("profile_img")
    private String profileImg;

    @SerializedName("lname")
    private String lname;

    @SerializedName("password")
    private String password;

    @SerializedName("social_id")
    private String socialId;

    @SerializedName("social_media_type_id")
    private String socialMediaTypeId;

    @SerializedName("dob")
    private String dob;

    @SerializedName("fcm_token")
    private String fcmToken;

    @SerializedName("pob")
    private String location;

    @SerializedName("pob_latlong")
    private String latlng;

    @SerializedName("email_id")
    private String email;

    @SerializedName("locale")
    private String locale;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("promo_id")
    private int promoId;

    @SerializedName("promo_code")
    private String promoCode;

    @SerializedName("marital_status")
    private String maritalStatus;


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

    public String getLocale() {
        return locale;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setSocialRegistrationStatus(int socialRegistrationStatus) {
        this.socialRegistrationStatus = socialRegistrationStatus;
    }

    public int getSocialRegistrationStatus() {
        return socialRegistrationStatus;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLname() {
        return lname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialMediaTypeId(String socialMediaTypeId) {
        this.socialMediaTypeId = socialMediaTypeId;
    }

    public String getSocialMediaTypeId() {
        return socialMediaTypeId;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getCountryCode() {
        if (countryCode==null){
            return "";
        }
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return
                "RegistrationRequestModel{" +
                        "fname = '" + fname + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",social_registration_status = '" + socialRegistrationStatus + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",device_type = '" + deviceType + '\'' +
                        ",otp = '" + otp + '\'' +
                        ",profile_img = '" + profileImg + '\'' +
                        ",lname = '" + lname + '\'' +
                        ",password = '" + password + '\'' +
                        ",social_id = '" + socialId + '\'' +
                        ",social_media_type_id = '" + socialMediaTypeId + '\'' +
                        ",dob = '" + dob + '\'' +
                        ",fcm_token = '" + fcmToken + '\'' +
                        ",location = '" + location + '\'' +
                        ",latlng = '" + latlng + '\'' +
                        ",email = '" + email + '\'' +
                        ",countryCode = '" + countryCode + '\'' +
                        ",promoId = '" + promoId + '\'' +
                        ",promoCode = '" + promoCode + '\'' +
                        "}";
    }
}