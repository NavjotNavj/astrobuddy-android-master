package in.appnow.astrobuddy.ui.fragments.your_chart.pager_items;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.horoscope_chart.IAPITaskCallBack;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.StringUtils;
import in.appnow.astrobuddy.utils.ToastUtils;

/**
 * Created by sonu on 16:08, 11/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeBasicDetailFragment extends Fragment implements IAPITaskCallBack {

    private static final String ARG_DOB = "dob";
    private static final String ARG_LATLONG = "lat_lng";
    private Context context;
    private Handler handler;
    private static final int SUCCESS = 99;

    @BindView(R.id.horoscope_detail_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.horoscope_detail_content_layout)
    LinearLayout horoscopeDetailLinearLayout;
    @BindView(R.id.horoscope_birth_detail_content_layout)
    LinearLayout birthDetailLinearLayout;
    @BindView(R.id.horoscope_panchang_detail_content_layout)
    LinearLayout panchangDetailLinearLayout;
    @BindView(R.id.basic_detail_label)
    TextView basicDetailLabel;
    @BindView(R.id.basic_panchang_detail_label)
    TextView panchangDetailLabel;
    @BindView(R.id.avakhada_detail_label)
    TextView avakhadaDetailLabel;

    private String dob, latLong;

    private static final int BASIC_DETAILS = 1;
    private static final int HOROSCOPE_DETAILS = 2;

    private int currentDetails = 0;

    public static HoroscopeBasicDetailFragment newInstance(String dob, String latLong) {

        Bundle args = new Bundle();
        args.putString(ARG_DOB, dob);
        args.putString(ARG_LATLONG, latLong);
        HoroscopeBasicDetailFragment fragment = new HoroscopeBasicDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dob = getArguments().getString(ARG_DOB);
            latLong = getArguments().getString(ARG_LATLONG);
        } else {
            ToastUtils.shortToast(R.string.unknown_error);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.horoscope_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        handler = new IncomingHandler();
        getHoroscopeDetails(HOROSCOPE_DETAILS);
    }

    private void getHoroscopeDetails(int detailType) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            this.currentDetails = detailType;
            String dateTimeString[] = dob.split(" ");
            String dateString[] = dateTimeString[0].split("-");
            String timeString[] = dateTimeString[1].split(":");
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
            if (detailType == HOROSCOPE_DETAILS)
                executeAPI("astro_details", formBody);
            else if (detailType == BASIC_DETAILS)
                executeAPI("birth_details", formBody);
        } catch (Exception e) {
            ToastUtils.shortToast("Failed to fetch data. Please try again.");
        }

    }

    private void executeAPI(String apiName, RequestBody formData) {
        String url = BuildConfig.HOROSCOPE_CHART_END_POINT + apiName;
        APITask apiTask = new APITask(context, url, BuildConfig.HOROSOPE_USER_ID, BuildConfig.HOROSOCPE_API_KEY, formData, this);
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
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                ToastUtils.shortToast(R.string.unknown_error);
            });
        }

    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String res = (String) msg.obj;
                    if (!TextUtils.isEmpty(res)) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Iterator<String> keys = jsonObject.keys();
                            LayoutInflater layoutInflater = LayoutInflater.from(context);
                            while (keys.hasNext()) {
                                String key = String.valueOf(keys.next()).trim();
                                String value = jsonObject.getString(key).trim();
                                if (!TextUtils.isEmpty(value)) {
                                    View view = layoutInflater.inflate(R.layout.horoscope_detail_content, null, false);
                                    TextView contentLabel = view.findViewById(R.id.horoscope_detail_content_label);
                                    TextView descLabel = view.findViewById(R.id.horoscope_detail_description_label);
                                    contentLabel.setText(StringUtils.convertFirstLetterCaps(key));
                                    descLabel.setText(StringUtils.convertFirstLetterCaps(value));

                                    if (currentDetails == HOROSCOPE_DETAILS) {
                                        horoscopeDetailLinearLayout.addView(view);
                                    } else if (currentDetails == BASIC_DETAILS) {
                                        birthDetailLinearLayout.addView(view);
                                    }

                                    if (key.equalsIgnoreCase("Tithi") || key.equalsIgnoreCase("Karan") || key.equalsIgnoreCase("Yog") || key.equalsIgnoreCase("Nakshatra") || key.equalsIgnoreCase("Sunrise") || key.equalsIgnoreCase("Sunset")) {
                                        if (view.getParent() != null)
                                            ((ViewGroup) view.getParent()).removeView(view);
                                        panchangDetailLinearLayout.addView(view);
                                    }
                                }
                            }
                            if (currentDetails == HOROSCOPE_DETAILS) {
                                getHoroscopeDetails(BASIC_DETAILS);
                            } else {
                                basicDetailLabel.setVisibility(View.VISIBLE);
                                panchangDetailLabel.setVisibility(View.VISIBLE);
                                avakhadaDetailLabel.setVisibility(View.VISIBLE);

                                birthDetailLinearLayout.setVisibility(View.VISIBLE);
                                horoscopeDetailLinearLayout.setVisibility(View.VISIBLE);
                                panchangDetailLinearLayout.setVisibility(View.VISIBLE);
                            }
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
