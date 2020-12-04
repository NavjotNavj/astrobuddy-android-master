package in.appnow.astrobuddy.ui.fragments.your_chart.pager_items;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.horoscope_chart.IAPITaskCallBack;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.StringUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import in.appnow.astrobuddy.utils.VectorUtils;

/**
 * Created by sonu on 16:08, 11/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeDashaFragment extends Fragment implements IAPITaskCallBack {

    private static final String ARG_DOB = "dob";
    private static final String ARG_LATLONG = "lat_lng";
    private static final String TAG = HoroscopeDashaFragment.class.getSimpleName();
    private Context context;
    private Handler handler;
    private static final int SUCCESS = 99;

    @BindView(R.id.dasha_progress_bar)
    ProgressBar dashaProgressBar;
    @BindView(R.id.horoscope_dasha_content_layout)
    LinearLayout linearLayout;
    @BindView(R.id.dasha_header_layout)
    LinearLayout headerLayout;
    @BindView(R.id.dasha_breadcrumbs_layout)
    LinearLayout breadCrumbsLayout;

    @BindArray(R.array.planet_names)
    String[] planetNameArray;

    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindColor(R.color.black)
    int blackColor;

    private String dob, latLong;

    private static final int MAJOR_DASHA = 1;
    private static final int SUB_DASHA = 2;
    private static final int SUB_SUB_DASHA = 3;

    private int currentDasha = 0;

    private String majorPlanetName = "", subPlanetName = "";

    public static HoroscopeDashaFragment newInstance(String dob, String latLong) {

        Bundle args = new Bundle();
        args.putString(ARG_DOB, dob);
        args.putString(ARG_LATLONG, latLong);
        HoroscopeDashaFragment fragment = new HoroscopeDashaFragment();
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
        return inflater.inflate(R.layout.dasha_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        handler = new IncomingHandler();
        fetchMajorDasha();
    }

    private void fetchMajorDasha() {
        majorPlanetName = "";
        getDashaData(MAJOR_DASHA, "major_vdasha");
    }

    private void getDashaData(int dashaType, String dashaName) {
        try {
            dashaProgressBar.setVisibility(View.VISIBLE);
            this.currentDasha = dashaType;
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
            executeAPI(dashaName, formBody);
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
                if (dashaProgressBar != null) {
                    dashaProgressBar.setVisibility(View.GONE);
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
                        Logger.DebugLog(TAG, "Fetch Data : " + res);
                        parseJsonData(res);
                    } else {
                        ToastUtils.shortToast(R.string.unknown_error);
                    }
                    dashaProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void parseJsonData(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (jsonArray.length() > 0) {
                linearLayout.removeAllViews();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String planetName = jsonObject.getString("planet");
                    int planetId = jsonObject.getInt("planet_id");
                    String startDate = jsonObject.getString("start");
                    String endDate = jsonObject.getString("end");

                    View view = layoutInflater.inflate(R.layout.horoscope_dasha_content, null, false);
                    TextView planetNameLabel = view.findViewById(R.id.horoscope_dasha_planet_label);
                    TextView startDateLabel = view.findViewById(R.id.horoscope_dasha_start_date_label);
                    TextView endDateLabel = view.findViewById(R.id.horoscope_dasha_end_date_label);
                    planetNameLabel.setText(StringUtils.convertFirstLetterCaps(planetName));

                    if (startDate.contains(" ")) {
                        String startDateSplit[] = startDate.split(" ");
                        startDateLabel.setText(startDateSplit[0]);
                    } else {
                        startDateLabel.setText(startDate);
                    }

                    if (endDate.contains(" ")) {
                        String endDateSplit[] = endDate.split(" ");
                        endDateLabel.setText(endDateSplit[0]);
                    } else {
                        endDateLabel.setText(startDate);
                    }

                    view.setOnClickListener(view1 -> {
                        switch (currentDasha) {
                            case MAJOR_DASHA:
                                fetchSubDasha(planetName);

                                break;
                            case SUB_DASHA:
                                if (!TextUtils.isEmpty(majorPlanetName)) {
                                    fetchSubSubDasha(majorPlanetName, planetName);
                                }
                                break;
                            case SUB_SUB_DASHA:
                                break;
                        }
                    });
                    switch (currentDasha) {
                        case MAJOR_DASHA:
                            addDashaBreadCrumbs("");
                            break;
                        case SUB_DASHA:
                            if (!TextUtils.isEmpty(majorPlanetName)) {
                                addDashaBreadCrumbs("");
                            }
                            break;
                        case SUB_SUB_DASHA:
                            addDashaBreadCrumbs(planetName);
                            break;
                    }
                    linearLayout.addView(view);
                }
                linearLayout.setVisibility(View.VISIBLE);
                headerLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.shortToast(R.string.unknown_error);
        }
    }

    private void fetchSubDasha(String planetName) {
        linearLayout.setVisibility(View.GONE);
        headerLayout.setVisibility(View.GONE);
        breadCrumbsLayout.setVisibility(View.GONE);
        this.majorPlanetName = planetName;
        subPlanetName = "";
        getDashaData(SUB_DASHA, "sub_vdasha/" + getPlaneName(planetName));
    }

    private void fetchSubSubDasha(String majorPlanetName, String subPlanetName) {
        linearLayout.setVisibility(View.GONE);
        headerLayout.setVisibility(View.GONE);
        breadCrumbsLayout.setVisibility(View.GONE);
        this.subPlanetName = subPlanetName;
        getDashaData(SUB_SUB_DASHA, "sub_sub_vdasha/" + getPlaneName(majorPlanetName) + "/" + getPlaneName(subPlanetName));
    }

    private void addDashaBreadCrumbs(String planetName) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        breadCrumbsLayout.removeAllViews();
        for (int i = 0; i <= currentDasha; i++) {
            View view = layoutInflater.inflate(R.layout.dasha_bread_crumbs_layout, null, false);
            TextView breadCrumbsLabel = view.findViewById(R.id.dasha_breadcrumbs_label);
            switch (i) {
                case MAJOR_DASHA:
                    if (!TextUtils.isEmpty(majorPlanetName)) {
                        breadCrumbsLabel.setText("Mahadasha (" + majorPlanetName + ")");
                        breadCrumbsLabel.setTextColor(blackColor);
                        VectorUtils.setVectorCompoundDrawable(breadCrumbsLabel, context, 0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0, R.color.colorPrimary);
                    } else {
                        breadCrumbsLabel.setTextColor(colorPrimary);
                        breadCrumbsLabel.setText("Mahadasha");
                        breadCrumbsLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    break;
                case SUB_DASHA:
                    if (!TextUtils.isEmpty(subPlanetName)) {
                        breadCrumbsLabel.setTextColor(blackColor);
                        breadCrumbsLabel.setText("Antar Dasha (" + subPlanetName + ")");
                        VectorUtils.setVectorCompoundDrawable(breadCrumbsLabel, context, 0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0, R.color.colorPrimary);
                    } else {
                        breadCrumbsLabel.setText("Antar Dasha");
                        breadCrumbsLabel.setTextColor(colorPrimary);
                        breadCrumbsLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    break;
                case SUB_SUB_DASHA:
                    breadCrumbsLabel.setTextColor(colorPrimary);
                    breadCrumbsLabel.setText("Pratyantar Dasha");
                    break;
            }

            int finalI = i;
            breadCrumbsLabel.setOnClickListener(view1 -> {
                switch (finalI) {
                    case MAJOR_DASHA:
                        fetchMajorDasha();
                        break;
                    case SUB_DASHA:
                        fetchSubDasha(majorPlanetName);
                        break;
                    case SUB_SUB_DASHA:
                        break;
                }
            });

            breadCrumbsLayout.addView(view);
        }
        breadCrumbsLayout.setVisibility(View.VISIBLE);
    }

    private String getPlaneName(String planetName) {
        if (new PreferenceManger(context.getSharedPreferences(AstroAppConstants.ASTRO_PREF_MANAGER, Context.MODE_PRIVATE)).getStringValue(PreferenceManger.ASTROLOGY_API_LANGUAGE).equalsIgnoreCase(APITask.HINDI_LANG)) {
            return StringUtils.getEnglishPlanetName(planetName.trim(), planetNameArray);
        }

        return planetName;
    }

}
