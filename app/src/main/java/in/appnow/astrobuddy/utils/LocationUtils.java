package in.appnow.astrobuddy.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sonu on 10:23, 27/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();

    public static String getAddressFromLatLng(Context context, LatLng latLng, String selectedAddress) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    // In this sample, get just a single address.
                    1);
        } catch (IOException | IllegalArgumentException ioException) {
            // Catch network or other I/O problems.
            ioException.printStackTrace();
            return selectedAddress;
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            return selectedAddress;
        } else {
            Address address = addresses.get(0);
            StringBuilder stringBuilder = new StringBuilder();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            //addressFragments.add(selectedAddress + ", ");
            String country = address.getCountryName();
            Logger.DebugLog(TAG, "Country Name : " + country);
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                Logger.DebugLog(TAG, "Address Line: " + address.getAddressLine(i) + "\n\n");
                // addressFragments.add(address.getAddressLine(i));
                if (country.equalsIgnoreCase("Singapore")) {
                    if (address.getAddressLine(i).equalsIgnoreCase(selectedAddress))
                        stringBuilder.append(address.getAddressLine(i));
                } else if (i == 2 || i == 3) {
                    if (i == 3) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(address.getAddressLine(i));
                }

            }
            if (TextUtils.isEmpty(stringBuilder.toString())) {
                stringBuilder.append(address.getLocality() + ", " + address.getCountryName());
            }
            Logger.DebugLog(TAG, "Picked address : " + stringBuilder.toString());
            return stringBuilder.toString();
        }
    }
}
