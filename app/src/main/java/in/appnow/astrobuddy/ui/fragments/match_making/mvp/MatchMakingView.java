package in.appnow.astrobuddy.ui.fragments.match_making.mvp;

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
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.rest.response.UserProfile;
import in.appnow.astrobuddy.ui.fragments.match_making.MatchMakingDetailFragment;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.KeyboardUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sonu on 12:26, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingView extends BaseViewClass implements BaseView {

    @BindView(R.id.match_making_dob_input_male)
    TextView dobInputMale;
    @BindView(R.id.match_making_tob_input_male)
    TextView tobInputMale;
    @BindView(R.id.match_making_location_male)
    TextView locationInputMale;
    @BindView(R.id.match_making_dob_input_female)
    TextView dobInputFemale;
    @BindView(R.id.match_making_tob_input_female)
    TextView tobInputFemale;
    @BindView(R.id.match_making_location_female)
    TextView locationInputFemale;
    @BindView(R.id.generate_details_button)
    Button generateDetailsButton;

    private String dateOfBirthMale = "";
    private String timeOfBirthMale = "";
    private String placeOfBirthMale = "";
    public String latLongMale = "";

    private String dateOfBirthFemale = "";
    private String timeOfBirthFemale = "";
    private String placeOfBirthFemale = "";
    public String latLongFemale = "";

    public static final int REQUEST_SELECT_PLACE = 1001;
    @BindString(R.string.play_service_package_name)
    String playServicePackageName;
    private FragmentManager fragmentManager;

    private boolean isFemale;

    public MatchMakingView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.fragment_match_making, this);
        ButterKnife.bind(this, this);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
    }

    public void autoPopulateUserDetails(@NonNull UserProfile userProfile) {

        dobInputMale.setText("");
        tobInputMale.setText("");
        locationInputMale.setText("");
        dobInputFemale.setText("");
        tobInputFemale.setText("");
        locationInputFemale.setText("");

        //2018-07-25 16:57:00
        String dobDisplay = DateUtils.parseStringDate(userProfile.getDob(), DateUtils.SERVER_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT);
        String dob = DateUtils.parseStringDate(userProfile.getDob(), DateUtils.SERVER_DATE_FORMAT, DateUtils.FORECAST_DATE_FORMAT);
        String splitDate[] = userProfile.getDob().split(" ");
        String[] splitTime = splitDate[1].split(":");
        String tob = splitTime[0] + ":" + splitTime[1];
        String latLong = userProfile.getLatlong();
        String location = userProfile.getLocation();
        if (userProfile.getGender().equalsIgnoreCase("M")) {
            locationInputMale.setText(location);
            dobInputMale.setText(dobDisplay);
            tobInputMale.setText(tob);
            latLongMale = latLong;
            dateOfBirthMale = dob;
        } else if (userProfile.getGender().equalsIgnoreCase("F")) {
            locationInputFemale.setText(location);
            dobInputFemale.setText(dobDisplay);
            tobInputFemale.setText(tob);
            latLongFemale = latLong;
            dateOfBirthFemale = dob;
        }
    }

    public Observable<Object> observeGenerateDetailsButton() {
        return RxView.clicks(generateDetailsButton);
    }

    public Observable<Object> observeDOBMale() {
        return RxView.clicks(dobInputMale);
    }

    public Observable<Object> observeTOBMale() {
        return RxView.clicks(tobInputMale);
    }

    public Observable<Object> observePlacePickMale() {
        return RxView.clicks(locationInputMale);
    }

    public Observable<Object> observeDOBFemale() {
        return RxView.clicks(dobInputFemale);
    }

    public Observable<Object> observeTOBFemale() {
        return RxView.clicks(tobInputFemale);
    }

    public Observable<Object> observePlacePickFemale() {
        return RxView.clicks(locationInputFemale);
    }

    public void openPlacesPicker(boolean isFemale) {
        this.isFemale = isFemale;
        KeyboardUtils.hideSoftKeyboard((AppCompatActivity) getContext());

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), RestUtils.getPlacesAPIKey());
        }
        try {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getContext());
            ((AppCompatActivity) getContext()).startActivityForResult(intent, REQUEST_SELECT_PLACE);
        } catch (Exception ignored) {

        }

    }

    private void showAlertDialogForPlayService(String message) {
        DialogHelperClass.showMessageOKCancel(getContext(), message, "OK", "CANCEL", (dialogInterface, i) -> {
            AppUtils.openPlayStore(getContext(), playServicePackageName);
        }, (dialogInterface, i) -> {

        });
    }

    public void setPlace(String place, LatLng latLng) {
        if (isFemale) {
            latLongFemale = latLng.latitude + "," + latLng.longitude;
            locationInputFemale.setText(place);
        } else {
            latLongMale = latLng.latitude + "," + latLng.longitude;
            locationInputMale.setText(place);
        }
    }


    public void showDatePicker(boolean isFemale) {
        this.isFemale = isFemale;
        KeyboardUtils.hideSoftKeyboard((AppCompatActivity) getContext());
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        SpinnerDatePickerDialogBuilder dialog = new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback((view, year1, monthOfYear, dayOfMonth) -> {
                    if (isFemale) {
                        dateOfBirthFemale = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        dobInputFemale.setText(DateUtils.parseStringDate(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1, DateUtils.SIMPLE_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT));
                    } else {
                        dateOfBirthMale = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        dobInputMale.setText(DateUtils.parseStringDate(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1, DateUtils.SIMPLE_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT));
                    }

                    showTimePickerDialog(isFemale);
                    //showHideTimePicker(VISIBLE);
                })
                .dialogTheme(R.style.DialogTheme)
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                //.showDay(true)
                .defaultDate(year, month, day)
                .maxDate(year, month, day)
                //.minDate(2000, 0, 1)
                ;

        com.tsongkha.spinnerdatepicker.DatePickerDialog datePickerDialog = dialog.build();
        datePickerDialog.show();

        //change ok anc cancel button color. It should call after dialog.show()
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }


    public void showTimePickerDialog(boolean isFemale) {
        this.isFemale = isFemale;
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
            if (isFemale) {
                tobInputFemale.setText(DateUtils.parseStringDate(picker.getDate().toString(), DateUtils.TIME_PICKED_FORMAT, DateUtils.TIME_FORMAT_24));
            } else {
                tobInputMale.setText(DateUtils.parseStringDate(picker.getDate().toString(), DateUtils.TIME_PICKED_FORMAT, DateUtils.TIME_FORMAT_24));
            }

        });
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public boolean validateData() {
        KeyboardUtils.hideSoftKeyboard((AppCompatActivity) getContext());
        String maleLocation = locationInputMale.getText().toString().trim();
        String maleDOB = dobInputMale.getText().toString().trim();
        String maleTOB = tobInputMale.getText().toString().trim();

        String femaleLocation = locationInputFemale.getText().toString().trim();
        String femaleDOB = dobInputFemale.getText().toString().trim();
        String femaleTOB = tobInputFemale.getText().toString().trim();

        if (TextUtils.isEmpty(maleLocation)) {
            ToastUtils.shortToast("Please enter male partner location.");
            return false;
        } else if (TextUtils.isEmpty(maleDOB)) {
            ToastUtils.shortToast("Please select male partner date of birth.");
            return false;
        } else if (TextUtils.isEmpty(maleTOB)) {
            ToastUtils.shortToast("Please select male partner time of birth.");
            return false;
        } else if (TextUtils.isEmpty(femaleLocation)) {
            ToastUtils.shortToast("Please enter female partner location.");
            return false;
        } else if (TextUtils.isEmpty(femaleDOB)) {
            ToastUtils.shortToast("Please select female partner date of birth.");
            return false;
        } else if (TextUtils.isEmpty(femaleTOB)) {
            ToastUtils.shortToast("Please select female partner time of birth.");
            return false;
        } else {
            FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, MatchMakingDetailFragment.newInstance(dateOfBirthMale, maleTOB, latLongMale, dateOfBirthFemale, femaleTOB, latLongFemale), FragmentUtils.MATCH_MAKING_DETAIl_FRAGMENT);
            /* MatchMakingDetailActivity.openMatchMakingDetailActivity(getContext(), dateOfBirthMale, maleTOB, latLongMale, dateOfBirthFemale, femaleTOB, latLongFemale);*/
            return true;
        }
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


}
