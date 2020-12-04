package in.appnow.astrobuddy.rest.response;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel extends BaseResponseModel implements Serializable {

    @SerializedName("social_accounts")
    private List<SocialAccountsLinked> socialAccounts;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("logged_in")
    private boolean loggedIn;

    @SerializedName("otp_match")
    private boolean otpMatched;

    @SerializedName("user_created")
    private boolean userCreated;

    @SerializedName("user_found")
    private boolean userFound;

    @SerializedName("user_profile")
    private UserProfile userProfile;



    public void setSocialAccounts(List<SocialAccountsLinked> socialAccounts) {
        this.socialAccounts = socialAccounts;
    }

    public List<SocialAccountsLinked> getSocialAccounts() {
        return socialAccounts;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setUserFound(boolean userFound) {
        this.userFound = userFound;
    }

    public boolean isUserFound() {
        return userFound;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public boolean isOtpMatched() {
        return otpMatched;
    }

    public void setOtpMatched(boolean otpMatched) {
        this.otpMatched = otpMatched;
    }

    public boolean isUserCreated() {
        return userCreated;
    }

    public void setUserCreated(boolean userCreated) {
        this.userCreated = userCreated;
    }

    @Override
    public String toString() {
        return
                "LoginResponseModel{" +
                        "social_accounts = '" + socialAccounts + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",logged_in = '" + loggedIn + '\'' +
                        ",user_found = '" + userFound + '\'' +
                        ",user_profile = '" + userProfile + '\'' +
                        "}";
    }
}