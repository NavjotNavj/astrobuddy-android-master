package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

public class CallPlanTypes {

    @SerializedName("duration")
    private int duration;

    @SerializedName("price")
    private int price;


    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return "CallPlanTypes{" +
                "duration='" + duration + '\'' +
                ", price=" + price +
                '}';
    }
}