package in.appnow.astrobuddy.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.text.TextUtils;

import in.appnow.astrobuddy.R;

/**
 * Created by sonu on 22:42, 24/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AppUtils {
    public static void shareData(Context context, String packageName, String subject, String sharingText) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");// Plain format text
        if (!packageName.equalsIgnoreCase("more"))
            sharingIntent.setPackage(packageName);

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharingText);
        try {
            context.startActivity(sharingIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
        }
    }

    public static void sendMessage(Context context, String sharingMessage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); //Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharingMessage);

            if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            context.startActivity(sendIntent);

        } else //For early versions, do what worked for you before.
        {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setData(Uri.parse("sms:"));
            viewIntent.putExtra("sms_body", sharingMessage);
            context.startActivity(viewIntent);
        }
    }

    public static void openPlayStore(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
        ((AppCompatActivity) context).finish();
    }

    public static void openWebLink(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static String getGender(String gender) {
        switch (gender) {
            case "M":
                return "Male";
            case "F":
                return "Female";
            default:
                return "Other";
        }
    }

    public static int getGenderIcon(String gender) {
        switch (gender) {
            case "M":
                return R.drawable.male;
            case "F":
                return R.drawable.female;
            default:
                return R.drawable.other;
        }
    }

    public static int getSunSignIcon(String sunSign) {
        switch (sunSign) {
            case "Aquarius":
                return R.drawable.aquarius_s;
            case "Pisces":
                return R.drawable.pisces_s;
            case "Aries":
                return R.drawable.aries_s;
            case "Taurus":
                return R.drawable.taurus_s;
            case "Gemini":
                return R.drawable.gemini_s;
            case "Cancer":
                return R.drawable.cancer_s;
            case "Leo":
                return R.drawable.leo_s;
            case "Virgo":
                return R.drawable.virgo_s;
            case "Libra":
                return R.drawable.libra_s;
            case "Scorpio":
                return R.drawable.scorpio_s;
            case "Sagittarius":
                return R.drawable.sagittarius_s;
            case "Capricorn":
                return R.drawable.capricorn_s;
            default:
                return R.drawable.aquarius_s;
        }
    }

    public static String getCurrencySymbol(String currency) {
        if (TextUtils.isEmpty(currency))
            return " ";
        switch (currency) {
            case "INR":
                return "`";
            case "GBP":
                return "£";
            default:
                return "$";
        }
    }

    public static void launchCustomTabUrl(Context context, String url) {
        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
// Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
// and launch the desired Url with CustomTabsIntent.launchUrl()

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
        // Changes the background color for the omnibox. colorInt is an int
// that specifies a Color.

        // builder.setToolbarColor(colorInt);


        // Adds an Action Button to the Toolbar.
// 'icon' is a Bitmap to be used as the image source for the
// action button.

// 'description' is a String be used as an accessible description for the button.

// 'pendingIntent is a PendingIntent to launch when the action button
// or menu item was tapped. Chrome will be calling PendingIntent#send() on
// taps after adding the url as data. The client app can call
// Intent#getDataString() to get the url.

// 'tint' is a boolean that defines if the Action Button should be tinted.

        //builder.setActionButton(icon, description, pendingIntent, tint);

        // builder.addMenuItem(menuItemTitle, menuItemPendingIntent);


        // builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        //builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
