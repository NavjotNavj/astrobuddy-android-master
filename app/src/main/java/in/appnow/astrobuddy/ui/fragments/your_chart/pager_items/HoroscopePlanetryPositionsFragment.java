package in.appnow.astrobuddy.ui.fragments.your_chart.pager_items;

import android.content.Context;
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
import androidx.fragment.app.Fragment;

import com.evrencoskun.tableview.TableView;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.horoscope_chart.IAPITaskCallBack;
import in.appnow.astrobuddy.models.Cell;
import in.appnow.astrobuddy.models.ColumnHeader;
import in.appnow.astrobuddy.models.PlanetryPositionsModel;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;

/**
 * Created by sonu on 16:08, 11/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopePlanetryPositionsFragment extends Fragment implements IAPITaskCallBack {

    private static final String ARG_DOB = "dob";
    private static final String ARG_LATLONG = "lat_lng";
    private static final String TAG = HoroscopePlanetryPositionsFragment.class.getSimpleName();
    private Context context;
    private Handler handler;
    private static final int SUCCESS = 99;

    @BindView(R.id.horoscope_planetry_positions_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.planetry_position_table_view)
    TableView tableView;

    private String dob, latLong;

    @BindArray(R.array.planetry_positions_table_columns)
    String[] planetryPositionsColumnArray;


    public static HoroscopePlanetryPositionsFragment newInstance(String dob, String latLong) {

        Bundle args = new Bundle();
        args.putString(ARG_DOB, dob);
        args.putString(ARG_LATLONG, latLong);
        HoroscopePlanetryPositionsFragment fragment = new HoroscopePlanetryPositionsFragment();
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
        return inflater.inflate(R.layout.horoscope_planetry_positions_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        handler = new IncomingHandler();
        progressBar.setVisibility(View.VISIBLE);
        getBasicDetails();
    }

    private void getBasicDetails() {
        try {
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
            executeAPI("planets", formBody);
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
                    try {
                        String res = (String) msg.obj;
                        if (!TextUtils.isEmpty(res)) {
                            try {
                                JSONArray jsonArray = new JSONArray(res);
                                if (jsonArray.length() > 0) {
                                    // LayoutInflater layoutInflater = LayoutInflater.from(context);
                                    List<List<Cell>> list = new ArrayList<>();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject innerJsonObject = jsonArray.getJSONObject(i);
                                        if (innerJsonObject != null) {
                                            List<Cell> cellList = new ArrayList<>();
                                            PlanetryPositionsModel model = new PlanetryPositionsModel();
                                            model.setId(innerJsonObject.getString("id"));

                                            model.setName(innerJsonObject.getString("name"));
                                            cellList.add(new Cell(model.getId(), model.getName()));
                                            try {
                                                model.setRetro(innerJsonObject.getBoolean("isRetro"));
                                                if (model.isRetro()) {
                                                    cellList.add(new Cell(model.getId(), "R"));
                                                } else {
                                                    cellList.add(new Cell(model.getId(), "-"));
                                                }
                                            } catch (Exception e) {
                                                model.setRetro(false);
                                            }

                                            model.setSign(innerJsonObject.getString("sign"));
                                            cellList.add(new Cell(model.getId(), model.getSign()));

                                            model.setSignLord(innerJsonObject.getString("signLord"));
                                            cellList.add(new Cell(model.getId(), model.getSignLord()));

                                            model.setFullDegree(innerJsonObject.getDouble("fullDegree"));

                                            model.setNormDegree(innerJsonObject.getDouble("normDegree"));
                                            cellList.add(new Cell(model.getId(), getDegree(String.valueOf(model.getNormDegree()))));

                                            model.setSpeed(innerJsonObject.getDouble("speed"));

                                            model.setNakshatra(innerJsonObject.getString("nakshatra"));
                                            cellList.add(new Cell(model.getId(), model.getNakshatra()));

                                            model.setNakshatraLord(innerJsonObject.getString("nakshatraLord"));
                                            cellList.add(new Cell(model.getId(), model.getNakshatraLord()));

                                            model.setHouse(innerJsonObject.getInt("house"));
                                            cellList.add(new Cell(model.getId(), String.valueOf(model.getHouse())));

                                            list.add(cellList);
                                        }

                                    }

                                    if (context != null && tableView != null) {
                                        PlanetryPositionsTableAdapter adapter = new PlanetryPositionsTableAdapter(context);
                                        tableView.setAdapter(adapter);
                                        tableView.setRowHeaderWidth(0);
                                        // tableView.setTableViewListener(new TableViewListener(tableView));
                                        adapter.setAllItems(getRandomColumnHeaderList(), null, list);
                                        tableView.setIgnoreSelectionColors(true);
                                        tableView.setTableViewListener(null);
                                        tableView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                                    }

                                } else {
                                    Logger.ErrorLog(TAG, "Json result is empty");
                                    ToastUtils.shortToast(R.string.unknown_error);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.shortToast(R.string.unknown_error);
                            }
                        } else {
                            ToastUtils.shortToast(R.string.unknown_error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.shortToast(R.string.unknown_error);
                    }
                    if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private String getDegree(String degree) {
        if (TextUtils.isEmpty(degree))
            return degree;
        if (degree.contains(".")) {
            String[] degrees = degree.split("\\.");
            if (degrees.length == 2) {
                if (TextUtils.isEmpty(degrees[1])) {
                    degrees[1] = "0";
                }

                double deg = Double.parseDouble(degrees[0]);

                double temp = Double.parseDouble("0." + degrees[1]);
                temp = temp * 3600;
                double min = Math.floor(temp / 60);
                double sec = Math.round(temp - (min * 60));

                if (deg < 10) deg = Double.parseDouble("0" + deg);
                if (min < 10) min = Double.parseDouble("0" + min);
                if (sec < 10) sec = Double.parseDouble("0" + sec);

                return (int) deg + " : " + (int) min + " : " + (int) sec;
            }
            return degree;
        }
        return degree;

    }


    private List<ColumnHeader> getRandomColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String title = planetryPositionsColumnArray[i];
            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }

        return list;
    }

}
