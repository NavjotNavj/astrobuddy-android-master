package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NILESH BHARODIYA on 10-09-2019.
 */
public class ResetPasswordRequest {

    @SerializedName("mobile")
    private String mobileNumber;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("new_password")
    private String newPassword;

    @SerializedName("otp")
    private String otp;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
