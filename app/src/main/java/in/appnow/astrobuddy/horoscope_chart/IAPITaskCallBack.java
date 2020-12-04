package in.appnow.astrobuddy.horoscope_chart;

/**
 * Created by Ashwani Janghu on 9/1/2015.
 */
public interface IAPITaskCallBack {

    void onSuccess(String response);
    void onFailure(String error);
}
