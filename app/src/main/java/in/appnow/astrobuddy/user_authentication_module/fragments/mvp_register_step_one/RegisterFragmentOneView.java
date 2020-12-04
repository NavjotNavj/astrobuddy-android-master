package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.jakewharton.rxbinding2.view.RxView;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.SpinnerCustomAdapter;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.KeyboardUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterFragmentOneView extends BaseViewClass {


    private static final String TAG = RegisterFragmentOneView.class.getSimpleName();
    @BindView(R.id.male_button)
    CircleImageView male_button;
    @BindView(R.id.female_button)
    CircleImageView female_button;
    @BindView(R.id.other_button)
    CircleImageView other_button;
    @BindView(R.id.f_name)
    AppCompatEditText f_name;
    @BindView(R.id.l_name)
    AppCompatEditText l_name;
    @BindView(R.id.dob_edittext)
    AppCompatTextView dob_edittext;
    @BindView(R.id.tob_edittext)
    AppCompatTextView tob_edittext;
    @BindView(R.id.pob_edittext)
    AppCompatAutoCompleteTextView pob_edittext;
    AppCompatActivity appCompatActivity;
    @BindView(R.id.male_label)
    TextView maleLabel;
    @BindView(R.id.female_label)
    TextView femaleLabel;
    @BindView(R.id.others_label)
    TextView othersLabel;
    @BindView(R.id.marital_status_spinner)
    AppCompatSpinner maritalStatusSpinner;

    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindColor(R.color.white)
    int whiteColor;

    @BindString(R.string.marital_status)
    String maritalStatusString;

    private String GENDER_TYPE = "";
    public static final int REQUEST_SELECT_PLACE = 1000;
    DatePickerDialog datePickerDialog;
    private String firstName = "";
    private String lastName = "";
    private String dateOfBirth = "";
    private String timeOfBirth = "";
    private String placeOfBirth = "";
    public String latLong = "";
    private String profileImage = "";
    private String socialMediaTypeId = "";
    private int socialRegistrationStatus = 0;
    private String socialID = "";
    private String maritalStatus = "";

    @BindString(R.string.play_service_package_name)
    String playServicePackageName;
    @BindArray(R.array.marital_status_array)
    String[] maritalStatusArray;

    @OnEditorAction(R.id.l_name)
    protected boolean actionDo(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            showDatePicker();
            return true;
        }
        return false;
    }


    public RegisterFragmentOneView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        inflate(getContext(), R.layout.registration_first_fragment, this);
        ButterKnife.bind(this, this);
        //selectGender(0);
        setWhiteColorToGenderOnInit();
        dob_edittext.setFocusable(false);
        tob_edittext.setFocusable(false);
        pob_edittext.setFocusable(false);

        setUpMaritalStatusSpinner();

        l_name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showDatePicker();
                return true;
            }
            return false;
        });
    }

    private void setUpMaritalStatusSpinner() {
        List<String> stringList = new ArrayList<>();
        stringList.add(maritalStatusString);
        stringList.addAll(Arrays.asList(maritalStatusArray));
        SpinnerCustomAdapter stringArrayAdapter = new SpinnerCustomAdapter(stringList, getContext());
        maritalStatusSpinner.setAdapter(stringArrayAdapter);
        maritalStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    maritalStatus = maritalStatusString;
                } else
                    maritalStatus = maritalStatusArray[i - 1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setPreData(String socialID, String socialMediaTypeID, String f_name, String l_name, String profileImage) {
        this.socialID = socialID;
        this.socialMediaTypeId = socialMediaTypeID;
        this.f_name.setText(f_name);
        this.l_name.setText(l_name);
        this.socialRegistrationStatus = 1;
        this.profileImage = profileImage;
    }


    public Observable<Object> observeMaleButton() {
        return RxView.clicks(male_button);
    }

    public Observable<Object> observeFemaleButton() {
        return RxView.clicks(female_button);
    }

    public Observable<Object> observeOtherButton() {
        return RxView.clicks(other_button);
    }

    public Observable<Object> observeDOBEdt() {
        return RxView.clicks(dob_edittext);
    }

    public Observable<Object> observeTimePickEdt() {
        return RxView.clicks(tob_edittext);
    }

    public Observable<Object> observePlacePickEdt() {
        return RxView.clicks(pob_edittext);
    }


    public void selectGender(int TAG) {
        if (TAG == 0) {
            GENDER_TYPE = "M";

            male_button.setBorderColor(appCompatActivity.getResources().getColor(R.color.colorPrimary));
            female_button.setBorderColor(appCompatActivity.getResources().getColor(android.R.color.transparent));
            other_button.setBorderColor(appCompatActivity.getResources().getColor(android.R.color.transparent));

            ImageUtils.changeImageColor(male_button, appCompatActivity, R.color.colorPrimary);
            ImageUtils.changeImageColor(female_button, appCompatActivity, R.color.white);
            ImageUtils.changeImageColor(other_button, appCompatActivity, R.color.white);

            maleLabel.setTextColor(colorPrimary);
            femaleLabel.setTextColor(whiteColor);
            othersLabel.setTextColor(whiteColor);

        } else if (TAG == 1) {
            GENDER_TYPE = "F";
            female_button.setBorderColor(appCompatActivity.getResources().getColor(R.color.colorPrimary));
            male_button.setBorderColor(appCompatActivity.getResources().getColor(android.R.color.transparent));
            other_button.setBorderColor(appCompatActivity.getResources().getColor(android.R.color.transparent));

            ImageUtils.changeImageColor(male_button, appCompatActivity, R.color.white);
            ImageUtils.changeImageColor(female_button, appCompatActivity, R.color.colorPrimary);
            ImageUtils.changeImageColor(other_button, appCompatActivity, R.color.white);


            maleLabel.setTextColor(whiteColor);
            femaleLabel.setTextColor(colorPrimary);
            othersLabel.setTextColor(whiteColor);

        } else if (TAG == 2) {
            GENDER_TYPE = "O";
            other_button.setBorderColor(appCompatActivity.getResources().getColor(R.color.colorPrimary));
            female_button.setBorderColor(appCompatActivity.getResources().getColor(android.R.color.transparent));
            male_button.setBorderColor(appCompatActivity.getResources().getColor(android.R.color.transparent));

            ImageUtils.changeImageColor(male_button, appCompatActivity, R.color.white);
            ImageUtils.changeImageColor(female_button, appCompatActivity, R.color.white);
            ImageUtils.changeImageColor(other_button, appCompatActivity, R.color.colorPrimary);

            maleLabel.setTextColor(whiteColor);
            femaleLabel.setTextColor(whiteColor);
            othersLabel.setTextColor(colorPrimary);

        }
    }

    private void setWhiteColorToGenderOnInit() {
        ImageUtils.changeImageColor(male_button, appCompatActivity, R.color.white);
        ImageUtils.changeImageColor(female_button, appCompatActivity, R.color.white);
        ImageUtils.changeImageColor(other_button, appCompatActivity, R.color.white);
    }

    public boolean validateData() {
        if (TextUtils.isEmpty(GENDER_TYPE)) {
            ToastUtils.shortToast("Please select gender.");
        } else if (TextUtils.isEmpty(maritalStatus) || maritalStatus.equalsIgnoreCase(maritalStatusString)) {
            ToastUtils.shortToast("Please select marital status.");
        } else if (f_name.getText().toString().trim().isEmpty() || f_name.getText().toString().trim().equalsIgnoreCase("null")) {
            ToastUtils.shortToast("Enter First Name");
        } else if (l_name.getText().toString().trim().isEmpty() || l_name.getText().toString().trim().equalsIgnoreCase("null")) {
            ToastUtils.shortToast("Enter Last Name");
        } else if (dob_edittext.getText().toString().isEmpty()) {
            ToastUtils.shortToast("Select Date Of Birth");
        } else if (tob_edittext.getText().toString().isEmpty()) {
            ToastUtils.shortToast("Select Time Of Birth");
        } else if (pob_edittext.getText().toString().isEmpty()) {
            ToastUtils.shortToast("Select Place Of Birth");
        } else {
            firstName = f_name.getText().toString().trim();
            lastName = l_name.getText().toString().trim();
            // dateOfBirth = dob_edittext.getText().toString();
            timeOfBirth = tob_edittext.getText().toString();
            placeOfBirth = pob_edittext.getText().toString();
            return true;
        }
        return false;
    }

    public void showDatePicker() {
        KeyboardUtils.hideSoftKeyboard(appCompatActivity);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        SpinnerDatePickerDialogBuilder dialog = new SpinnerDatePickerDialogBuilder()
                .context(appCompatActivity)
                .callback((view, year1, monthOfYear, dayOfMonth) -> {
                    dateOfBirth = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    dob_edittext.setText(DateUtils.parseStringDate(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1, DateUtils.SIMPLE_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT));
                    showTimePickerDialog();
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

    public void openPlacesPicker() {
        KeyboardUtils.hideSoftKeyboard(appCompatActivity);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), RestUtils.getPlacesAPIKey());
        }
        try {

            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(appCompatActivity);
            appCompatActivity.startActivityForResult(intent, REQUEST_SELECT_PLACE);
        }
        catch (Exception ignored){

        }
    }

    private void showAlertDialogForPlayService(String message) {
        DialogHelperClass.showMessageOKCancel(appCompatActivity, message, "OK", "CANCEL", (dialogInterface, i) -> {
            AppUtils.openPlayStore(appCompatActivity, playServicePackageName);
        }, (dialogInterface, i) -> {

        });
    }

    public void setPlace(String place, LatLng latLng) {
        latLong = latLng.latitude + "," + latLng.longitude;
        pob_edittext.setText(place);

    }

    public String getSocialID() {
        return socialID;
    }

    public String getSocialMediaTypeId() {
        return socialMediaTypeId;
    }

    public int getSocialRegistrationStatus() {
        return socialRegistrationStatus;
    }

    public String getGender() {
        return GENDER_TYPE;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getTimeOfBirth() {
        return DateUtils.get24HourFormat(timeOfBirth);
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getLatLong() {
        return latLong;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void showTimePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity, R.style.DialogTheme);
        SingleDateAndTimePicker picker = new SingleDateAndTimePicker(appCompatActivity);
        picker.setPadding(0, 50, 0, 20);
        picker.setVisibleItemCount(5);
        picker.setDefaultDate(new Date(System.currentTimeMillis()));
        picker.setCyclic(true);
        // picker.setCurved(true);
        picker.setStepMinutes(1);
        builder.setView(picker);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            tob_edittext.setText(DateUtils.parseStringDate(picker.getDate().toString(), DateUtils.TIME_PICKED_FORMAT, DateUtils.TIME_FORMAT));
        });
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
