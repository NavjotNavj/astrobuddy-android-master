package in.appnow.astrobuddy.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class StringUtils {

    // Convert ArrayList into String
    public static String convertStringArrayListToString(ArrayList<String> list) {

        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (String s : list) {
            sb.append(delim);
            sb.append(s);
            delim = ",";
        }
        return sb.toString();
    }

    // Convert Strings into ArrayList
    public static ArrayList<String> convertStringToStringArrayList(String string) {

        return new ArrayList<>(Arrays.asList(string
                .split(",")));
    }

    // Convert Strings into ArrayList
    public static ArrayList<Integer> convertStringToIntegerArrayList(String string) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        String[] splitString = string.split(",");
        for (String s : splitString) {
            arrayList.add(Integer.parseInt(s));
        }
        return arrayList;

    }

    public static String truncateLastCharacterFromString(String value, int charToTruncate) {
        if (TextUtils.isEmpty(value))
            return value;
        return value.substring(0, value.length() - charToTruncate);
    }

    public static String extractNumberFromString(String value) {
        return value.replaceAll("\\D+", "");
    }

    public static String[] splitString(String value, String delimiter) {
        if (!TextUtils.isEmpty(value)) {
            if (value.contains(delimiter)) {
                return value.split(delimiter);
            }
        }
        return new String[]{value};
    }

    public static int getCurrentSelectedItemFromStringArray(String[] listItems, String currentItem) {
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i].equalsIgnoreCase(currentItem)) {
                return i;
            }
        }

        return -1;
    }

    public static Long[] toPrimitives(long... objects) {

        Long[] primitives = new Long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];

        return primitives;
    }

    public static String convertFirstLetterCaps(String value) {
        try {
            value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();

        } catch (Exception ignored) {

        }
        return value;
    }

    public static String getEnglishPlanetName(String value, String[] planetNameArray) {
        try {
            if (!TextUtils.isEmpty(value) && planetNameArray.length > 0) {
                for (String planetName : planetNameArray) {
                    String[] split = planetName.split(",");
                    if (value.equalsIgnoreCase(split[1])) {
                        return split[0];
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return value;
    }
}
