package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallPlan {

    @SerializedName("plan_id")
    private String planId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String desc;

    @SerializedName("img_file")
    private String imageFile;

    @SerializedName("exp")
    private String experience;

    @SerializedName("language")
    private String language;

    @SerializedName("per_minute_price")
    private String perMinPrice;

    @SerializedName("available_status")
    private int available;

    @SerializedName("rating")
    private double rating;

    @SerializedName("plan_types")
    private List<CallPlanTypes> callPlanTypes;

    private String mobileNumber;
    private String countryCode;


    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getRating() {
        return rating;
    }

    public List<CallPlanTypes> getCallPlanTypes() {
        return callPlanTypes;
    }

    public String getPlanId() {
        return planId;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getExperience() {
        return experience;
    }

    public String getLanguage() {
        return language;
    }

    public String getPerMinPrice() {
        return perMinPrice;
    }

    public int getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "CallPlan{" +
                "planId='" + planId + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", experience='" + experience + '\'' +
                ", language='" + language + '\'' +
                ", perMinPrice='" + perMinPrice + '\'' +
                ", available=" + available +
                ", rating=" + rating +
                ", callPlanTypes=" + callPlanTypes +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}