package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 11/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterMobileRequest {

    @SerializedName("mobile")
    private String mobileNumber;
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("email_id")
    private String emailId;
    @SerializedName("fname")
    private String firstName;
    @SerializedName("lname")
    private String lastName;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
