package in.appnow.astrobuddy.rest.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("gender")
    private String gender;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("social_registration_status")
    private String socialRegistrationStatus;

    @SerializedName("dob")
    private String dob;

    @SerializedName("pob_latlong")
    private String latlong;

    @SerializedName("fcm_token")
    private String fcmToken;

    @SerializedName("active")
    private String active;

    @SerializedName("pob")
    private String location;

    @SerializedName("email")
    private String email;

    @SerializedName("mobile")
    private String mobileNumber;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("profile_img")
    private String profileImage;

    @SerializedName("star_sign")
    private String starSign;

    @SerializedName("locale")
    private String locale;

    @SerializedName("marital_status")
    private String maritalStatus;

    @SerializedName("X-access-token")
    private String userToken;

    @SerializedName("X-node-token")
    private String nodeToken;

    public String getNodeToken() {
        return nodeToken;
    }

    public void setNodeToken(String nodeToken) {
        this.nodeToken = nodeToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLname() {
        return lname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setSocialRegistrationStatus(String socialRegistrationStatus) {
        this.socialRegistrationStatus = socialRegistrationStatus;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getSocialRegistrationStatus() {
        return socialRegistrationStatus;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFullName() {
        if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname)) {
            return fname + " " + lname;
        } else if (!TextUtils.isEmpty(fname)) {
            return fname;
        } else return "";
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStarSign() {
        return starSign;
    }

    public void setStarSign(String starSign) {
        this.starSign = starSign;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return
                "UserProfile{" +
                        "fname = '" + fname + '\'' +
                        ",lname = '" + lname + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",social_registration_status = '" + socialRegistrationStatus + '\'' +
                        ",dob = '" + dob + '\'' +
                        ",latlong = '" + latlong + '\'' +
                        ",fcm_token = '" + fcmToken + '\'' +
                        ",active = '" + active + '\'' +
                        ",location = '" + location + '\'' +
                        ",email = '" + email + '\'' +
                        ",countryCode = '" + countryCode + '\'' +
                        ",profileImage = '" + profileImage + '\'' +
                        ",starSign = '" + starSign + '\'' +
                        ",locale = '" + locale + '\'' +
                        "}";
    }
}