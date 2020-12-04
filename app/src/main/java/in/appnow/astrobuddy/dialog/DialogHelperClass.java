package in.appnow.astrobuddy.dialog;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import androidx.appcompat.app.AlertDialog.Builder;

import in.appnow.astrobuddy.R;


public class DialogHelperClass {
    public static void showMessageOKCancel(Context context, String message, String positiveButton, String negativeButton, OnClickListener okListener, OnClickListener cancelListener) {
        new Builder(context, R.style.DialogTheme)
                .setMessage(message)
                .setPositiveButton(positiveButton, okListener)
                .setNegativeButton(negativeButton, cancelListener)
                .setCancelable(false)
                .create()
                .show();
    }
    public static void showMessageOKCancel(Context context,
                                           String title,
                                           String message,
                                           String positiveButton,
                                           String negativeButton,
                                           OnClickListener okListener,
                                           OnClickListener cancelListener) {
        new Builder(context, R.style.DialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, okListener)
                .setNegativeButton(negativeButton, cancelListener)
                .setCancelable(false)
                .create()
                .show();
    }

    public static void showListDialog(Context context, String title, String[] itemList, OnClickListener okListener) {
        new Builder(context, R.style.DialogTheme)
                .setTitle(title)
                .setItems(itemList, okListener)
                .setCancelable(true)
                .create()
                .show();
    }

    public static void showImageDialogWithOkButton(Context context, String title, String message, int icon, String positiveButton, OnClickListener okListener) {
        new Builder(context, R.style.DialogTheme)
                .setTitle(title)
                .setIcon(icon)
                .setMessage(message)
                .setPositiveButton(positiveButton, okListener)
                .setCancelable(true)
                .create()
                .show();
    }

    public static void showSingleChoiceListDialog(Context context, String title, String[] itemList, int checkedItem,String positiveButton,String negativeButton, OnClickListener selectListener,OnClickListener okListener) {
        new Builder(context, R.style.DialogTheme)
                .setTitle(title)
                .setSingleChoiceItems(itemList, checkedItem,selectListener)
                .setPositiveButton(positiveButton,okListener)
                .setNegativeButton(negativeButton,null)
                .setCancelable(true)
                .create()
                .show();
    }
}
