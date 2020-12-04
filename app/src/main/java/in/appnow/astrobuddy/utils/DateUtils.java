package in.appnow.astrobuddy.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import android.util.Log;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Abhishek on 22-08-2017.
 */

public class DateUtils {

    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SERVER_ONLY_DATE_FORMAT = "dd-MM-yyyy";
    public static final String SIMPLE_DATE_FORMAT_WITH_TIME = "dd/MM/yyyy HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String FORECAST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DOB_DATE_FORMAT = "dd MMM, yyyy";
    public static final String TIME_PICKED_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
    public static final String TIME_FORMAT = "hh:mm a";
    public static final String TIME_FORMAT_24 = "HH:mm";
    public static final String TRANSACTION_FORMAT = "dd MMM yyyy, hh:mm aa";
    public static final String CHAT_HISTORY_DATE_FORMAT = "hh:mmaa dd-MMM-yy";
    public static final String SINGLE_CHAT_DATE_FORMAT = "dd MMM yy";

    public static final String CHAT_DISPLAY_DATE_FORMAT = "hh:mm a";
    public static final String CHAT_COPY_DATE_FORMAT = "[dd/mm, hh:mm aa]";
    public static final String POST_STATS_HOUR_FORMAT = "HH";

    public static long secondsInMilli = 1000;
    public static long minutesInMilli = secondsInMilli * 60;
    public static long hoursInMilli = minutesInMilli * 60;
    public static long daysInMilli = hoursInMilli * 24;


    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date = new Date(milliSeconds);
        return formatter.format(date.getTime());
    }

    /**
     * Return date in specified format.
     *
     * @param dateToConvert    Date in string
     * @param inputDateFormat  Input Date format
     * @param outputDateFormat Output Date format
     * @return String representing date in specified format
     */
    public static String parseStringDate(String dateToConvert, String inputDateFormat, String outputDateFormat) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputDateFormat, Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateToConvert);
            return outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return dateToConvert;
        }
    }

    /**
     * Return date in specified format.
     *
     * @param dateToConvert Date in string
     * @return String representing date in specified format
     */
    public static String getDOBTime(String dateToConvert) {
        checkNotNull(dateToConvert);
        String splitDate[] = dateToConvert.split(" ");
        String[] splitTime = splitDate[1].split(":");
        String amPM = "";
        if (Integer.parseInt(splitTime[0]) >= 12) {
            amPM = " PM";
        } else {
            amPM = " AM";
        }
        return splitTime[0] + " : " + splitTime[1] + amPM;
    }

    public static String getDateWithSuffix(String date) {
        checkNotNull(date);
        String dateSplit[] = date.split(" ");
        String getDayWithSuffix = getDayOfMonthSuffix(Integer.parseInt(dateSplit[0]));
        return dateSplit[0] + getDayWithSuffix + " " + dateSplit[1] + " " + dateSplit[2];
    }

    public static String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getAge(String dob, String inputDateFormat) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault());

        try {
            Date dobDate = inputFormat.parse(dob);
            Date currentDate = new Date(System.currentTimeMillis());
            String dobYear = outputFormat.format(dobDate);
            String currentYear = outputFormat.format(currentDate);
            String[] splitDobDate = dobYear.split("/");
            String[] splitCurrentDate = currentYear.split("/");

            int year = Integer.parseInt(splitCurrentDate[2]) - Integer.parseInt(splitDobDate[2]);

            if (year == 0) {
                int month = Integer.parseInt(splitCurrentDate[1]) - Integer.parseInt(splitDobDate[1]);
                if (month == 0) {
                    int day = Integer.parseInt(splitCurrentDate[0]) - Integer.parseInt(splitDobDate[0]);
                    if (day == 0) {
                        return "0 Yrs";
                    } else {
                        return day + " Days";
                    }
                } else {
                    return month + " Months";

                }

            } else {
                return year + " Yrs";
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "0 Yrs";
        }
    }


    public static String getAMPM(int hourOfDay, int minute) {
        String time = null;
        if (hourOfDay > 12) {
            time = String.valueOf(hourOfDay - 12) + ":" + (String.valueOf(minute) + "PM");
        }
        if (hourOfDay == 12) {
            time = "12" + ":" + (String.valueOf(minute) + "PM");
        }
        if (hourOfDay < 12) {
            time = String.valueOf(hourOfDay) + ":" + (String.valueOf(minute) + "AM");
        }
        return time;
    }


    public static String getTime(int hourOfDay, int minute) {
        String time;
        if (hourOfDay > 9) {
            if (minute < 10) {
                time = "" + getHour(hourOfDay) + ":0" + minute + " " + "PM";
            } else {
                time = "" + getHour(hourOfDay) + ":" + minute + " " + "PM";
            }
        } else {
            if (minute < 10) {
                time = "0" + getHour(hourOfDay) + ":0" + minute + " " + "AM";
            } else {
                time = "0" + getHour(hourOfDay) + ":" + minute + " " + "AM";
            }
        }
        return time;
    }

    public static int getHour(int hourOfDay) {
        int hours = 0;
        if (hourOfDay > 12) {
            hours = hourOfDay - 12;
        } else {
            hours = hourOfDay;
        }
        return hours;
    }


    public static String get24HourFormat(String time){
        String inputPattern = "hh:mm a";
        String outputPattern = "HH:mm";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        try {
            return outputFormat.format(inputFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String parseRegistrationDateFormat(String time) {
        String inPattern = "EEE MMM dd HH:mm:ss Z yyyy";
        String outputPattern = "hh:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return str;
    }


    public static String parseFeedBackDateFormat(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "dd MMM, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return str;
    }

    public static String parseServerDateFormatInAMPM(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "dd-MM-yyyy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String parseRewardsDate(String time) {
        String inputPattern = "yyyy/MM/dd HH:mm:ss";
        String outputPattern = "dd MMM , yyyy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getFormattedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        switch (day % 10) {
            case 1:
                return new SimpleDateFormat("MMMM d'st', yyyy").format(date);
            case 2:
                return new SimpleDateFormat("MMMM d'nd', yyyy").format(date);
            case 3:
                return new SimpleDateFormat("MMMM d'rd', yyyy").format(date);
            default:
                return new SimpleDateFormat("MMMM d'th', yyyy").format(date);
        }
    }

    public static String parseEndDateFormat(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Date minus15MinutesToDate(Date beforeTime) {
        final long ONE_MINUTE_IN_MILLIS = 60000;
        long curTimeInMs = beforeTime.getTime();
        return new Date(curTimeInMs - (15 * ONE_MINUTE_IN_MILLIS));
    }

    public static Date addMintuesToDate(Date beforeTime, int minutes) {
        final long ONE_MINUTE_IN_MILLIS = 60000;
        long curTimeInMs = beforeTime.getTime();
        return new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
    }


    public static Date minusHoursToDate(int hour, Date beforeTime) {
        final long ONE_HOUR_IN_MILLIS = 3600000;
        long curTimeInMs = beforeTime.getTime();
        return new Date(curTimeInMs - (hour * ONE_HOUR_IN_MILLIS));
    }


    public static String dateDiffrence(String today, String lastDate) {

        //Specify the data format
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        long diff = 0;

        try {

            //Convert to Date
            Date startDate = df.parse(today);
            Calendar c1 = Calendar.getInstance();
            //Change to Calendar Date
            c1.setTime(startDate);

            //Convert to Date
            Date endDate = df.parse(lastDate);
            Calendar c2 = Calendar.getInstance();
            //Change to Calendar Date
            c2.setTime(endDate);

            //get Time in milli seconds
            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();
            //get difference in milli seconds
            diff = ms2 - ms1;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Find number of days by dividing the mili seconds
        int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
        System.out.println("Number of days difference is: " + diffInDays);

        return "" + diffInDays;
        //To get number of seconds diff/1000
        //To get number of minutes diff/(1000 * 60)
        //To get number of hours diff/(1000 * 60 * 60)

    }


    public static String parseDate(String parseDate) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(parseDate);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String parseTime(String parseDate) {
        String inputPattern = "hh:mm:ss";
        String outputPattern = "hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(parseDate);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDateAndTime(Long timestamp, String dateFormat) {
        Date date = new Date(timestamp);
        SimpleDateFormat fullDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return fullDateFormat.format(date);
    }

    public static String convertDateForChatDisplay(Date dateToConvert, String toDateFormat) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(toDateFormat, Locale.getDefault());
        return outputFormat.format(dateToConvert);
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static long getDateDifference(String startDate, String endDate, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());

        try {
            Date getStartDate = simpleDateFormat.parse(startDate);
            Date getEndDate = simpleDateFormat.parse(endDate);

            //milliseconds
            long dateDifference = getEndDate.getTime() - getStartDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("dateDifference : " + dateDifference);


            long elapsedDays = dateDifference / daysInMilli;
            // dateDifference = dateDifference % daysInMilli;

            long elapsedHours = dateDifference / hoursInMilli;
            //  dateDifference = dateDifference % hoursInMilli;

            long elapsedMinutes = dateDifference / minutesInMilli;
            //  dateDifference = dateDifference % minutesInMilli;

            long elapsedSeconds = dateDifference / secondsInMilli;

            System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
            return dateDifference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;

    }

    public static boolean isYesterday(String date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            Date d = simpleDateFormat.parse(date);
            return android.text.format.DateUtils.isToday(d.getTime() + android.text.format.DateUtils.DAY_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isToday(String date_time) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(date_time);
            System.out.println("Today is " + date.getTime());
            return android.text.format.DateUtils.isToday(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
