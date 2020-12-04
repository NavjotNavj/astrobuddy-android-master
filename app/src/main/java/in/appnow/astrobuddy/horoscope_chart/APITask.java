package in.appnow.astrobuddy.horoscope_chart;

/**
 * @author Vedic Rishi Astro Pvt Ltd
 * @description API Task Thread for a calling Vedic Rishi Astro API
 */

import android.content.Context;
import android.text.TextUtils;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.helper.PreferenceManger;

public class APITask implements Runnable {

    public static final String HINDI_LANG = "hi";
    public static final String ENGLISH_LANG = "en";

    private String apiKey;
    private String userId;
    public String url;
    private String lang;
    private Context context;
    RequestBody body;
    IAPITaskCallBack callBack;
    private final OkHttpClient client = new OkHttpClient();

    public APITask(Context context, String URL, String userId, String publicAPIKey, RequestBody body, IAPITaskCallBack callBack) {
        this.context = context;
        this.apiKey = publicAPIKey;
        this.userId = userId;
        this.url = URL;
        this.body = body;
        this.callBack = callBack;
    }

    private void callAPI() {

        try {
            lang = new PreferenceManger(context.getSharedPreferences(AstroAppConstants.ASTRO_PREF_MANAGER, Context.MODE_PRIVATE)).getStringValue(PreferenceManger.ASTROLOGY_API_LANGUAGE);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.setAuthenticator(new Authenticator() {

                public Request authenticate(Proxy proxy, Response response) {
                    String credential = Credentials.basic(userId, apiKey);
                    return response.request().newBuilder().header("Authorization", credential)
                            .header("Accept-language", TextUtils.isEmpty(lang) ? ENGLISH_LANG : lang)
                            .build();
                }

                public Request authenticateProxy(Proxy proxy, Response response) {
                    return null;
                }
            });

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                callBack.onFailure("Request Failed with Error : " + response.code());
            } else {
                callBack.onSuccess(response.body().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        callAPI();
    }


}

