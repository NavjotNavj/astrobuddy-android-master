package in.appnow.astrobuddy.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.helper.glide.GlideApp;
import in.appnow.astrobuddy.rest.RestUtils;

/**
 * Created by sonu on 14:41, 19/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ImageUtils {
    public static void setBackgroundImage(Context context, ImageView imageView) {
        try {
            if (context == null || imageView == null)
                return;
            GlideApp.with(context).load(R.drawable.app_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDrawableImage(Context context, ImageView imageView, int res) {
        GlideApp.with(context).load(res)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void changeImageColor(ImageView imageView, Context context, int color) {
        imageView.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
    }

    public static Drawable changeShapeColor(Context context, int drawable, int color) {
        Drawable mDrawable = ContextCompat.getDrawable(context, drawable);
        if (mDrawable != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_IN));
        }
        return mDrawable;
    }

    public static void loadImageUrl(Context context, ImageView imageView, int placeHolder, String imageLink) {
        if (context == null || TextUtils.isEmpty(imageLink) || imageView == null)
            return;
        String imageNewLink = "";
        if (!imageLink.contains("http")) {
            imageNewLink = RestUtils.getEndPoint() + imageLink;
        } else imageNewLink = imageLink;
        GlideApp.with(context).load(imageNewLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.signature(new ObjectKey(System.currentTimeMillis()))
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(imageView);
    }

    public static Bitmap convertImagePathToBitmap(String imagePath, int width, int height) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, o);
        if (width == 0 || height == 0) {
            return Bitmap.createBitmap(bitmap);
        }
        // Logger.DebugLog("IMAGE SIZE ", "SIZE : " + bitmap.getByteCount());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        // Logger.DebugLog("IMAGE SIZE ", "SCALED SIZE : " + scaledBitmap.getByteCount());
        return scaledBitmap;

       /* BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(imagePath)),null, o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Logger.DebugLog("IMAGE SIZE ", "SIZE : " + bitmap.getByteCount());
        //Find the correct scale value. It should be the power of 2.
        int scale = 1;
        if (width == 0 || height == 0) {
            return Bitmap.createBitmap(bitmap);
        }
        while (o.outWidth / scale / 2 >= width && o.outHeight / scale / 2 >= height)
            scale *= 2;

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(imagePath, o2);
        Logger.DebugLog("IMAGE SIZE ", "SCALED SIZE : " + scaledBitmap.getByteCount());
        return scaledBitmap;*/
    }

    public static String convertBitmapIntoBase64(Bitmap bitmap) {
        if (bitmap == null)
            return "";
        return Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            }
            if (cursor != null) {
                cursor.moveToFirst();
            }
            String string = null;
            if (cursor != null) {
                string = cursor.getString(column_index);
            }
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
