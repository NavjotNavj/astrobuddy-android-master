package in.appnow.astrobuddy.ui.fragments.panchang.input.mvp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.jakewharton.rxbinding2.view.RxView;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.ui.fragments.panchang.PanchangFragment;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.KeyboardUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 11:48, 17/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PanchangInputView extends BaseViewClass {

    @BindView(R.id.panchang_location)
    TextView locationInput;
    @BindView(R.id.panchang_date_input)
    TextView dateInput;
    @BindView(R.id.panchang_time_input)
    TextView timeInput;
    @BindView(R.id.show_panchang_button)
    Button showPanchangButton;

    private String latLong, date = "";

    public static final int REQUEST_SELECT_PLACE = 1002;
    @BindString(R.string.play_service_package_name)
    String playServicePackageName;

    private FragmentManager fragmentManager;

    public PanchangInputView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.fragment_panchang_input, this);
        ButterKnife.bind(this, this);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        populateData();
    }

    private void populateData() {
        String date = DateUtils.getDate(System.currentTimeMillis(), DateUtils.DOB_DATE_FORMAT);
        String displayDate = DateUtils.getDate(System.currentTimeMillis(), DateUtils.FORECAST_DATE_FORMAT);
        this.date = displayDate;
        dateInput.setText(date);

        String time = DateUtils.getDate(System.currentTimeMillis(), DateUtils.TIME_FORMAT_24);
        timeInput.setText(time);
    }

    public void setSavedLocation(@NonNull PreferenceManger preferenceManger) {
        String panchangLocation = preferenceManger.getStringValue(PreferenceManger.PANCHANG_LOCATION);
        String panchangLatLng = preferenceManger.getStringValue(PreferenceManger.PANCHANG_LATLNG);
        if (!TextUtils.isEmpty(panchangLocation)) {
            locationInput.setText(panchangLocation);
        }
        if (!TextUtils.isEmpty(panchangLatLng)) {
            latLong = panchangLatLng;
        }
    }

    public Observable<Object> observeShowPanchangButton() {
        return RxView.clicks(showPanchangButton);
    }

    public Observable<Object> observeDate() {
        return RxView.clicks(dateInput);
    }

    public Observable<Object> observeTime() {
        return RxView.clicks(timeInput);
    }

    public Observable<Object> observePlacePick() {
        return RxView.clicks(locationInput);
    }

    public void openPlacesPicker() {
        KeyboardUtils.hideSoftKeyboard((AppCompatActivity) getContext());

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), RestUtils.getPlacesAPIKey());
        }
        try {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getContext());
            ((AppCompatActivity) getContext()).startActivityForResult(intent, REQUEST_SELECT_PLACE);
        }
        catch (Exception ignored){

        }
    }

    private void showAlertDialogForPlayService(String message) {
        DialogHelperClass.showMessageOKCancel(getContext(), message, "OK", "CANCEL", (dialogInterface, i) -> {
            AppUtils.openPlayStore(getContext(), playServicePackageName);
        }, (dialogInterface, i) -> {

        });
    }

    public void setPlace(String place, LatLng latLng) {
        latLong = latLng.latitude + "," + latLng.longitude;
        locationInput.setText(place);
    }

    public void showDatePicker() {
        KeyboardUtils.hideSoftKeyboard((AppCompatActivity) getContext());
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        SpinnerDatePickerDialogBuilder dialog = new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback((view, year1, monthOfYear, dayOfMonth) -> {
                    date = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    dateInput.setText(DateUtils.parseStringDate(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1, DateUtils.SIMPLE_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT));

                    showTimePickerDialog();
                    //showHideTimePicker(VISIBLE);
                })
                .dialogTheme(R.style.DialogTheme)
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                //.showDay(true)
                .defaultDate(year, month, day)
                //.maxDate(year, month, day)
                //.minDate(2000, 0, 1)
                ;

        com.tsongkha.spinnerdatepicker.DatePickerDialog datePickerDialog = dialog.build();
        datePickerDialog.show();

        //change ok anc cancel button color. It should call after dialog.show()
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void showTimePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
        SingleDateAndTimePicker picker = new SingleDateAndTimePicker(getContext());
        picker.setPadding(0, 50, 0, 20);
        picker.setVisibleItemCount(5);
        picker.setDefaultDate(new Date(System.currentTimeMillis()));
        picker.setCyclic(true);
        // picker.setCurved(true);
        picker.setStepMinutes(1);
        builder.setView(picker);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            timeInput.setText(DateUtils.parseStringDate(picker.getDate().toString(), DateUtils.TIME_PICKED_FORMAT, DateUtils.TIME_FORMAT_24));

        });
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public boolean validateData() {
        KeyboardUtils.hideSoftKeyboard((AppCompatActivity) getContext());
        String location = locationInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();
        if (TextUtils.isEmpty(location)) {
            ToastUtils.shortToast("Please enter panchang place.");
            return false;
        } else if (TextUtils.isEmpty(date)) {
            ToastUtils.shortToast("Please select panchang date.");
            return false;
        } else if (TextUtils.isEmpty(time)) {
            ToastUtils.shortToast("Please select panchang time.");
            return false;
        } else {
            FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, PanchangFragment.newInstance(this.date, time, location, latLong), FragmentUtils.PANCHANG_FRAGMENT);
            return true;
        }
    }

}
