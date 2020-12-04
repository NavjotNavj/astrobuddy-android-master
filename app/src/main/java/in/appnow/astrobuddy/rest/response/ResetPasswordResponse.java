package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NILESH BHARODIYA on 11-09-2019.
 */
public class ResetPasswordResponse extends BaseResponseModel {

    @SerializedName("otp_match")
    private boolean otpMatch;

    @SerializedName("password_updated")
    private boolean passwordUpdated;

    public boolean isOtpMatch() {
        return otpMatch;
    }

    public boolean isPasswordUpdated() {
        return passwordUpdated;
    }

    @Override
    public String toString() {
        return "ResetPasswordResponse{" +
                "otpMatch=" + otpMatch +
                ", passwordUpdated=" + passwordUpdated +
                '}';
    }
}
