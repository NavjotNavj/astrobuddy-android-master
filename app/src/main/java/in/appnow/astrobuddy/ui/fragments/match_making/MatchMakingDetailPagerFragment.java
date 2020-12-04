package in.appnow.astrobuddy.ui.fragments.match_making;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.evrencoskun.tableview.TableView;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.horoscope_chart.IAPITaskCallBack;
import in.appnow.astrobuddy.models.Cell;
import in.appnow.astrobuddy.models.ColumnHeader;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingDetailView;
import in.appnow.astrobuddy.ui.fragments.your_chart.pager_items.PlanetryPositionsTableAdapter;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.StringUtils;
import in.appnow.astrobuddy.utils.ToastUtils;

/**
 * Created by sonu on 18:46, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingDetailPagerFragment extends Fragment implements IAPITaskCallBack {
    protected static final String ARG_API_NAME = "api_name";
    protected static final String ARG_DOB_MALE = "dob_male";
    protected static final String ARG_TOB_MALE = "tob_male";
    protected static final String ARG_LAT_LNG_MALE = "lat_lng_male";
    protected static final String ARG_DOB_FEMALE = "dob_female";
    protected static final String ARG_TOB_FEMALE = "tob_female";
    protected static final String ARG_LAT_LNG_FEMALE = "lat_lng_female";
    private Unbinder unbinder;

    @BindView(R.id.match_making_detail_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.match_making_detail_table_view)
    TableView tableView;
    @BindView(R.id.match_making_pager_nested_scroll_view)
    NestedScrollView nestedScrollView;


    private static final int SUCCESS = 99;
    private static final String TAG = MatchMakingDetailPagerFragment.class.getSimpleName();

    private String apiName, dobMale, tobMale, latLngMale, dobFemale, tobFemale, latLngFemale;

    private Handler handler;

    @BindArray(R.array.ashtakoot_table_columns)
    String[] ashtakootColumnArray;
    @BindArray(R.array.dashakoot_table_columns)
    String[] dashakootColumnArray;

    public static MatchMakingDetailPagerFragment newInstance(String apiName, String dobMale, String tobMale, String latLngMale, String dobFemale, String tobFemale, String latlngFemale) {

        Bundle args = new Bundle();

        args.putString(ARG_API_NAME, apiName);
        args.putString(ARG_DOB_MALE, dobMale);
        args.putString(ARG_TOB_MALE, tobMale);
        args.putString(ARG_LAT_LNG_MALE, latLngMale);
        args.putString(ARG_DOB_FEMALE, dobFemale);
        args.putString(ARG_TOB_FEMALE, tobFemale);
        args.putString(ARG_LAT_LNG_FEMALE, latlngFemale);

        MatchMakingDetailPagerFragment fragment = new MatchMakingDetailPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apiName = getArguments().getString(ARG_API_NAME);
            dobMale = getArguments().getString(ARG_DOB_MALE);
            tobMale = getArguments().getString(ARG_TOB_MALE);
            latLngMale = getArguments().getString(ARG_LAT_LNG_MALE);
            dobFemale = getArguments().getString(ARG_DOB_FEMALE);
            tobFemale = getArguments().getString(ARG_TOB_FEMALE);
            latLngFemale = getArguments().getString(ARG_LAT_LNG_FEMALE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.match_making_detail_pager_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        handler = new IncomingHandler();
        getMatchMakingDetails();
    }

    public void getMatchMakingDetails() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            String dateMaleString[] = dobMale.split("-");
            String timeMaleString[] = tobMale.split(":");
            String latLongMaleString[] = latLngMale.split(",");

            String dateFemaleString[] = dobFemale.split("-");
            String timeFemaleString[] = tobFemale.split(":");
            String latLongFemaleString[] = latLngFemale.split(",");
            RequestBody formBody = new FormEncodingBuilder()
                    .add("m_day", dateMaleString[2])
                    .add("m_month", dateMaleString[1])
                    .add("m_year", dateMaleString[0])
                    .add("m_hour", timeMaleString[0])
                    .add("m_min", timeMaleString[1])
                    .add("m_lat", latLongMaleString[0])
                    .add("m_lon", latLongMaleString[1])
                    .add("m_tzone", "5.5")
                    .add("f_day", dateFemaleString[2])
                    .add("f_month", dateFemaleString[1])
                    .add("f_year", dateFemaleString[0])
                    .add("f_hour", timeFemaleString[0])
                    .add("f_min", timeFemaleString[1])
                    .add("f_lat", latLongFemaleString[0])
                    .add("f_lon", latLongFemaleString[1])
                    .add("f_tzone", "5.5")
                    .build();
            // the first argument is the API name
            // for ex- planets or astro_details or general_house_report/sun
            // you can get the API names from https://www.vedicrishiastro.com/docs
            // NOTE: please make sure there is no / before the API name.
            // for example /astro_details will give an error
            executeAPI(apiName, formBody);

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
        if (getContext() == null)
            return;
        ((AppCompatActivity) getContext()).runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            ToastUtils.shortToast(R.string.unknown_error);
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
                            List<List<Cell>> list = new ArrayList<>();
                            while (keys.hasNext()) {
                                List<Cell> cellList = new ArrayList<>();
                                String key = String.valueOf(keys.next()).trim();
                                if (!key.equalsIgnoreCase("conclusion")) {

                                    cellList.add(new Cell(key, StringUtils.convertFirstLetterCaps(key)));

                                    JSONObject valueObject = jsonObject.getJSONObject(key);
                                    String description = "-", maleKootPoints = "-", femaleKootPoints = "-";
                                    int totalPoints = 0, receivedPoints = 0;
                                    if (valueObject != null) {

                                        if (apiName.equalsIgnoreCase(MatchMakingDetailView.ASHTAKOOT_URL)) {
                                            if (valueObject.has("description"))
                                                description = valueObject.getString("description");
                                            cellList.add(new Cell(key, description));
                                        }

                                        if (valueObject.has("male_koot_attribute"))
                                            maleKootPoints = valueObject.getString("male_koot_attribute");
                                        cellList.add(new Cell(key, maleKootPoints));


                                        if (valueObject.has("female_koot_attribute"))
                                            femaleKootPoints = valueObject.getString("female_koot_attribute");
                                        cellList.add(new Cell(key, femaleKootPoints));


                                        if (valueObject.has("total_points"))
                                            totalPoints = valueObject.getInt("total_points");
                                        cellList.add(new Cell(key, String.valueOf(totalPoints)));

                                        if (valueObject.has("received_points"))
                                            receivedPoints = valueObject.getInt("received_points");
                                        cellList.add(new Cell(key, String.valueOf(receivedPoints)));


                                        list.add(cellList);
                                    }
                                }
                            }
                            populateTableView(list);

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

    private void populateTableView(List<List<Cell>> list) {
        if (getContext() != null && tableView != null) {
            PlanetryPositionsTableAdapter adapter = new PlanetryPositionsTableAdapter(getContext());
            tableView.setAdapter(adapter);
            tableView.setRowHeaderWidth(0);
            adapter.setAllItems(getRandomColumnHeaderList(), null, list);
            tableView.setIgnoreSelectionColors(true);
            tableView.setTableViewListener(null);
            tableView.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }

    private List<ColumnHeader> getRandomColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();
        if (apiName.equalsIgnoreCase(MatchMakingDetailView.ASHTAKOOT_URL)) {
            for (int i = 0; i < ashtakootColumnArray.length; i++) {
                String title = ashtakootColumnArray[i];
                ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
                list.add(header);
            }

        } else {
            for (int i = 0; i < dashakootColumnArray.length; i++) {
                String title = dashakootColumnArray[i];
                ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
                list.add(header);
            }
        }

        return list;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
