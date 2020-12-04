package in.appnow.astrobuddy.ui.fragments.your_chart.pager_items;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.custom_views.ZoomLayout;
import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.horoscope_chart.ChartViewNorth;
import in.appnow.astrobuddy.horoscope_chart.IAPITaskCallBack;
import in.appnow.astrobuddy.ui.fragments.your_chart.ZoomChartActivity;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;

import static android.view.View.VISIBLE;

/**
 * Created by sonu on 16:08, 11/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopePlanetryChartFragment extends Fragment implements IAPITaskCallBack {

    private static final String ARG_DOB = "dob";
    private static final String ARG_LATLONG = "lat_lng";

    private Context context;

    private Handler handler;
    private static final int SUCCESS = 99;

    @BindView(R.id.lagna_chart_view)
    FrameLayout lagnaChartView;
    @BindView(R.id.navansh_chart_view)
    FrameLayout navanshChartView;
    @BindView(R.id.moon_chart_view)
    FrameLayout moonChartView;
    @BindView(R.id.chalit_chart_view)
    FrameLayout chalitChartView;

    private String dob, latLong;

    final String[] values = new String[]{"D1", "chalit", "MOON", "SUN", "D2",
            "D3", "D4", "D5", "D7", "D8", "D9", "D10",
            "D12", "D16", "D20", "D24", "D27", "D30", "D40",
            "D45", "D60"};


    private int[] mLagnaCurrentRashiArr = null;
    private String[] mLagnaCurrentPlanetStr = null;

    private int[] mNavanshCurrentRashiArr = null;
    private String[] mNavanshCurrentPlanetStr = null;

    private int[] mMoonCurrentRashiArr = null;
    private String[] mMoonCurrentPlanetStr = null;

    private int[] mChalitCurrentRashiArr = null;
    private String[] mChalitCurrentPlanetStr = null;

    private static final int LAGNA_CHART = 1;
    private static final int NAVANSH_CHART = 2;
    private static final int MOON_CHART = 3;
    private static final int CHALIT_CHART = 4;

    private int currentChartFetching = 0;


    public static HoroscopePlanetryChartFragment newInstance(String dob, String latLong) {

        Bundle args = new Bundle();
        args.putString(ARG_DOB, dob);
        args.putString(ARG_LATLONG, latLong);
        HoroscopePlanetryChartFragment fragment = new HoroscopePlanetryChartFragment();
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
        if (getArguments()!=null){
            dob = getArguments().getString(ARG_DOB);
            latLong = getArguments().getString(ARG_LATLONG);
        }
        else{
            ToastUtils.shortToast(R.string.unknown_error);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.planetry_chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        handler = new IncomingHandler();
        getApiInfo("D1", LAGNA_CHART);
    }

    @OnClick({R.id.lagna_chart_view, R.id.navansh_chart_view, R.id.moon_chart_view, R.id.chalit_chart_view})
    void implementClickEvent(View view) {
        switch (view.getId()) {
            case R.id.lagna_chart_view:
                if (mLagnaCurrentRashiArr != null && mLagnaCurrentPlanetStr != null) {
                    ZoomChartActivity.startZoomChartActivity(context, mLagnaCurrentRashiArr, mLagnaCurrentPlanetStr);
                }
                break;
            case R.id.navansh_chart_view:
                if (mNavanshCurrentRashiArr != null && mNavanshCurrentPlanetStr != null) {
                    ZoomChartActivity.startZoomChartActivity(context, mNavanshCurrentRashiArr, mNavanshCurrentPlanetStr);
                }
                break;
            case R.id.moon_chart_view:
                if (mMoonCurrentRashiArr != null && mMoonCurrentPlanetStr != null) {
                    ZoomChartActivity.startZoomChartActivity(context, mMoonCurrentRashiArr, mMoonCurrentPlanetStr);
                }
                break;
            case R.id.chalit_chart_view:
                if (mChalitCurrentRashiArr != null && mChalitCurrentPlanetStr != null) {
                    ZoomChartActivity.startZoomChartActivity(context, mChalitCurrentRashiArr, mChalitCurrentPlanetStr);
                }
                break;
        }
    }

    public void getApiInfo(String chartId, int chartType) {
        try {
            this.currentChartFetching = chartType;
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
            executeAPI("horo_chart/" + chartId, formBody);
        } catch (Exception e){
            ToastUtils.shortToast("Failed to fetch data. Please try again.");
        }
    }

    private void executeAPI(String apiName, RequestBody formData) {
        String url = BuildConfig.HOROSCOPE_CHART_END_POINT + apiName;
        APITask apiTask = new APITask(context,url, BuildConfig.HOROSOPE_USER_ID, BuildConfig.HOROSOCPE_API_KEY, formData, this);
        Thread thread = new Thread(apiTask);
        thread.start();
    }

    @Override
    public void onSuccess(String response) {
        Message message = Message.obtain();
        message.what = SUCCESS;
        message.obj = response;
        message.arg1 = currentChartFetching;
        handler.sendMessage(message);
    }

    @Override
    public void onFailure(String error) {
        if (getActivity()!=null) {
            getActivity().runOnUiThread(() -> {
                ToastUtils.shortToast(R.string.unknown_error);
            });
        }

    }


    @SuppressLint("HandlerLeak")
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String res = (String) msg.obj;
                    int currentChart = msg.arg1;
                    setChartData(res, currentChart);
                    switch (currentChart) {
                        case LAGNA_CHART:
                            getApiInfo("D9", NAVANSH_CHART);
                            break;
                        case NAVANSH_CHART:
                            getApiInfo("MOON", MOON_CHART);
                            break;
                        case MOON_CHART:
                            getApiInfo("chalit", CHALIT_CHART);
                            break;

                    }
                    break;
            }
        }
    }

    private void setChartData(String jsonData, int currentChart) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);

            int[] rashiArr = populateRashiArr(jsonArray);
            String[] planetStr = populatePlanetStr(jsonArray);

            FrameLayout chartLayout = null;
            switch (currentChart) {
                case LAGNA_CHART:
                    chartLayout = lagnaChartView;
                    mLagnaCurrentPlanetStr = planetStr;
                    mLagnaCurrentRashiArr = rashiArr;
                    break;
                case NAVANSH_CHART:
                    chartLayout = navanshChartView;
                    mNavanshCurrentPlanetStr = planetStr;
                    mNavanshCurrentRashiArr = rashiArr;
                    break;
                case MOON_CHART:
                    chartLayout = moonChartView;
                    mMoonCurrentPlanetStr = planetStr;
                    mMoonCurrentRashiArr = rashiArr;
                    break;
                case CHALIT_CHART:
                    chartLayout = chalitChartView;
                    mChalitCurrentPlanetStr = planetStr;
                    mChalitCurrentRashiArr = rashiArr;
                    break;
            }
            if (chartLayout != null) {
                drawChart(chartLayout, rashiArr, planetStr);
                chartLayout.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawChart(FrameLayout chartLayout, int[] mCurrentRashiArr, String[] mCurrentPlanetStr) {
        Display display = ((AppCompatActivity) context).getWindowManager().getDefaultDisplay();
        ChartViewNorth view = new ChartViewNorth(context, display.getWidth(), mCurrentRashiArr, mCurrentPlanetStr);
        view.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(), display.getWidth()));
        chartLayout.addView(view);
    }

    private int[] populateRashiArr(JSONArray data) {
        int[] rashi = new int[12];
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                rashi[i] = Integer.parseInt(obj.getString("sign"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rashi;
    }

    private String[] populatePlanetStr(JSONArray jsonArray) {
        ArrayList<JSONArray> planets = new ArrayList<>();
        String[] retArr = new String[12];
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray arr = obj.getJSONArray("planet");
                //Log.i("CHART_DEMO", ""+arr.toString());
                planets.add(arr);
            }

            String lang = Locale.getDefault().getLanguage();

            for (int j = 0; j < planets.size(); j++) {
                JSONArray jsonArray1 = planets.get(j);
                String str = "";
                for (int k = 0; k < jsonArray1.length(); k++) {
                    String temp = jsonArray1.getString(k);
                    if (lang.equals("hi")) {
                        str += temp;
                    } else if (null != temp && temp.length() > 0) {
                        str += temp.substring(0, 2) + " ";
                    }
                }
                retArr[j] = str;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("CHART_RET", ""+ Arrays.toString(retArr));
        return retArr;
    }
}
