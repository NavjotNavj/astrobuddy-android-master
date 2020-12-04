package in.appnow.astrobuddy.ui.fragments.panchang.mvp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.horoscope_chart.IAPITaskCallBack;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.StringUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 13:09, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PanchangView extends BaseViewClass implements BaseView, IAPITaskCallBack {

    private static final int SUCCESS = 99;
    private static final String TAG = PanchangView.class.getSimpleName();
    private Handler handler;

    @BindView(R.id.panchang_detail_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.panchang_nested_Scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.panchang_date_label)
    TextView panchangTodayDateLabel;
    @BindView(R.id.panchang_detail_content_layout)
    LinearLayout detailContentLayout;
    @BindView(R.id.panchang_inauspicious_period_detail_content_layout)
    LinearLayout inauspiciousPeriodContentLayout;
    @BindView(R.id.panchang_lunar_month_content_layout)
    LinearLayout lunarMonthContentLayout;
    @BindView(R.id.panchang_chat_button)
    FloatingActionButton chatButton;

    public PanchangView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.panchang_fragment, this);
        ButterKnife.bind(this, this);
        handler = new IncomingHandler();
    }

    public Observable<Object> observeChatButton() {
        return RxView.clicks(chatButton);
    }

    @Override
    public void onUnknownError(String error) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {

    }


    public void getPanchangDetails(String date, String time, String latLong) {
        try {
            nestedScrollView.setVisibility(View.GONE);

            panchangTodayDateLabel.setText("Panchang For " + date);

            progressBar.setVisibility(View.VISIBLE);
            String dateString[] = date.split("-");
            String timeString[] = time.split(":");
            String latLongString[] = latLong.split(",");
            RequestBody formBody = new FormEncodingBuilder()
                    .add("day", dateString[2])
                    .add("month", dateString[1])
                    .add("year", dateString[0])
                    .add("hour", timeString[0])
                    .add("min", timeString[1])
                    .add("lat", latLongString[0])
                    .add("lon", latLongString[1])
                    .add("tzone", "5.5")
                    .build();
            // the first argument is the API name
            // for ex- planets or astro_details or general_house_report/sun
            // you can get the API names from https://www.vedicrishiastro.com/docs
            // NOTE: please make sure there is no / before the API name.
            // for example /astro_details will give an error
            executeAPI("advanced_panchang", formBody);

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.shortToast("Failed to fetch data. Please try again.");
        }

    }

    private void executeAPI(String apiName, RequestBody formData) {
        String url = BuildConfig.HOROSCOPE_JSON_END_POINT + apiName;
        APITask apiTask = new APITask(getContext(), url, BuildConfig.HOROSOPE_USER_ID, BuildConfig.HOROSOCPE_API_KEY, formData, this);
        Thread thread = new Thread(apiTask);
        thread.start();
    }

    @Override
    public void onSuccess(String response) {
        Message message = Message.obtain();
        message.what = SUCCESS;
        message.obj = response;
        handler.sendMessage(message);
    }

    @Override
    public void onFailure(String error) {
        ((AppCompatActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                ToastUtils.shortToast(R.string.unknown_error);
            }
        });

    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String res = (String) msg.obj;
                    if (!TextUtils.isEmpty(res)) {
                        Logger.DebugLog(TAG, "DATA : " + res);
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Iterator<String> keys = jsonObject.keys();
                            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                            detailContentLayout.removeAllViews();
                            inauspiciousPeriodContentLayout.removeAllViews();
                            lunarMonthContentLayout.removeAllViews();
                            while (keys.hasNext()) {
                                String key = String.valueOf(keys.next()).trim();
                                Object value = jsonObject.get(key);

                                View view = layoutInflater.inflate(R.layout.horoscope_detail_content, null, false);
                                TextView contentLabel = view.findViewById(R.id.horoscope_detail_content_label);
                                TextView descLabel = view.findViewById(R.id.horoscope_detail_description_label);

                                if (value instanceof JSONObject) {
                                    JSONObject valueObject = new JSONObject(String.valueOf(value));
                                    if (key.equalsIgnoreCase("tithi")) {
                                        JSONObject detailsObject = valueObject.getJSONObject("details");
                                        JSONObject endTimeObject = valueObject.getJSONObject("end_time");

                                        contentLabel.setText(StringUtils.convertFirstLetterCaps(key));
                                        descLabel.setText(detailsObject.getString("tithi_name") + " TILL " + endTimeObject.getString("hour") + ":" + endTimeObject.getString("minute"));

                                        detailContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("nakshatra")) {
                                        JSONObject detailsObject = valueObject.getJSONObject("details");
                                        JSONObject endTimeObject = valueObject.getJSONObject("end_time");

                                        contentLabel.setText(StringUtils.convertFirstLetterCaps(key));
                                        descLabel.setText(detailsObject.getString("nak_name") + " TILL " + endTimeObject.getString("hour") + ":" + endTimeObject.getString("minute"));

                                        detailContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("yog")) {
                                        JSONObject detailsObject = valueObject.getJSONObject("details");
                                        JSONObject endTimeObject = valueObject.getJSONObject("end_time");

                                        contentLabel.setText(StringUtils.convertFirstLetterCaps(key));
                                        descLabel.setText(detailsObject.getString("yog_name") + " TILL " + endTimeObject.getString("hour") + ":" + endTimeObject.getString("minute"));

                                        detailContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("karan")) {
                                        JSONObject detailsObject = valueObject.getJSONObject("details");
                                        JSONObject endTimeObject = valueObject.getJSONObject("end_time");

                                        contentLabel.setText(StringUtils.convertFirstLetterCaps(key));
                                        descLabel.setText(detailsObject.getString("karan_name") + " TILL " + endTimeObject.getString("hour") + ":" + endTimeObject.getString("minute"));

                                        detailContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("rahukaal")) {
                                        contentLabel.setText("Rahu Kalam");
                                        descLabel.setText(valueObject.getString("start") + " : " + valueObject.getString("end"));
                                        inauspiciousPeriodContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("yamghant_kaal")) {
                                        contentLabel.setText("Yamaganda Kalam");
                                        descLabel.setText(valueObject.getString("start") + " : " + valueObject.getString("end"));
                                        inauspiciousPeriodContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("guliKaal")) {
                                        contentLabel.setText("Gulika Kalam");
                                        descLabel.setText(valueObject.getString("start") + " : " + valueObject.getString("end"));
                                        inauspiciousPeriodContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("abhijit_muhurta")) {
                                        contentLabel.setText("Abhijit Muhurta");
                                        descLabel.setText(valueObject.getString("start") + " : " + valueObject.getString("end"));

                                        detailContentLayout.addView(view);

                                    } else if (key.equalsIgnoreCase("hindu_maah")) {
                                        contentLabel.setText("Adhik Mass");
                                        descLabel.setText(valueObject.getBoolean("adhik_status") ? "YES" : "NO");
                                        detailContentLayout.addView(view);

                                        View view1 = layoutInflater.inflate(R.layout.horoscope_detail_content, null, false);
                                        TextView contentLabel1 = view1.findViewById(R.id.horoscope_detail_content_label);
                                        TextView descLabel1 = view1.findViewById(R.id.horoscope_detail_description_label);
                                        contentLabel1.setText("Amanta");
                                        descLabel1.setText(valueObject.getString("amanta"));

                                        lunarMonthContentLayout.addView(view1);

                                        View view2 = layoutInflater.inflate(R.layout.horoscope_detail_content, null, false);
                                        TextView contentLabel2 = view2.findViewById(R.id.horoscope_detail_content_label);
                                        TextView descLabel2 = view2.findViewById(R.id.horoscope_detail_description_label);
                                        contentLabel2.setText("Purnimanta");
                                        descLabel2.setText(valueObject.getString("purnimanta"));
                                        lunarMonthContentLayout.addView(view2);

                                    }
                                } else {
                                    String stringValue = jsonObject.getString(key).trim();
                                    if (key.equalsIgnoreCase("Moonrise") || key.equalsIgnoreCase("Moonset") || key.equalsIgnoreCase("Sun_Sign") || key.equalsIgnoreCase("Moon_Sign") || key.equalsIgnoreCase("Sunrise") || key.equalsIgnoreCase("Sunset") || key.equalsIgnoreCase("Ritu") || key.equalsIgnoreCase("Ayana")) {
                                        if (key.contains("_")) {
                                            key = key.replace("_", " ");
                                        }
                                        contentLabel.setText(StringUtils.convertFirstLetterCaps(key));
                                        descLabel.setText(StringUtils.convertFirstLetterCaps(stringValue));
                                        detailContentLayout.addView(view);
                                    }
                                    if (key.equalsIgnoreCase("shaka_samvat")) {
                                        contentLabel.setText("Shaka Smavat");
                                        descLabel.setText(jsonObject.getString("shaka_samvat") + " - " + jsonObject.getString("shaka_samvat_name"));
                                        detailContentLayout.addView(view);
                                    }
                                    if (key.equalsIgnoreCase("vikram_samvat")) {
                                        contentLabel.setText("Vikram Smavat");
                                        descLabel.setText(jsonObject.getString("vikram_samvat") + " - " + jsonObject.getString("vkram_samvat_name"));
                                        detailContentLayout.addView(view);
                                    }

                                    if (key.equalsIgnoreCase("paksha")) {
                                        contentLabel.setText(StringUtils.convertFirstLetterCaps(key));
                                        descLabel.setText(stringValue);
                                        lunarMonthContentLayout.addView(view);
                                    }
                                }


                            }
                            nestedScrollView.setVisibility(View.VISIBLE);
                            nestedScrollView.scrollTo(0, 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.shortToast(R.string.unknown_error);
                        }
                    } else {
                        ToastUtils.shortToast(R.string.unknown_error);
                    }
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
