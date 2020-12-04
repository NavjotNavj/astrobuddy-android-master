package in.appnow.astrobuddy.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;

import static in.appnow.astrobuddy.app.AstroAppConstants.ITEM_COUNT;

/**
 * Created by sonu on 12:22, 24/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SunSignModel implements Parcelable {
    private int sunId;
    private String sunSignName, romanName;
    private int sunIcon;

    public SunSignModel() {
    }

    public int getSunId() {
        return sunId;
    }

    public String getSunSignName() {
        return sunSignName;
    }

    public String getRomanName() {
        return romanName;
    }

    public int getSunIcon() {
        return sunIcon;
    }

    public SunSignModel(int sunId, String sunSignName, String romanName, int sunIcon) {
        this.sunId = sunId;
        this.sunSignName = sunSignName;
        this.romanName = romanName;
        this.sunIcon = sunIcon;
    }

    private static final int[] icons = {R.drawable.aries, R.drawable.taurus, R.drawable.gemini, R.drawable.cancer,
            R.drawable.leo, R.drawable.virgo, R.drawable.libra, R.drawable.scorpion, R.drawable.sagi, R.drawable.capricorn,
            R.drawable.aquarius, R.drawable.pisces};

    protected SunSignModel(Parcel in) {
        sunId = in.readInt();
        sunSignName = in.readString();
        romanName = in.readString();
        sunIcon = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sunId);
        dest.writeString(sunSignName);
        dest.writeString(romanName);
        dest.writeInt(sunIcon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SunSignModel> CREATOR = new Creator<SunSignModel>() {
        @Override
        public SunSignModel createFromParcel(Parcel in) {
            return new SunSignModel(in);
        }

        @Override
        public SunSignModel[] newArray(int size) {
            return new SunSignModel[size];
        }
    };

    public static List<SunSignModel> getSunSignList(Context context) {
        List<SunSignModel> sunSignModelList = new ArrayList<>();
        String[] sunSignArray = context.getResources().getStringArray(R.array.sun_sign_array);
        for (int i = 0; i < ITEM_COUNT; i++) {
            String[] sunSignSplit = sunSignArray[i].split(",");
            sunSignModelList.add(new SunSignModel(Integer.parseInt(sunSignSplit[0]), sunSignSplit[1], sunSignSplit[2], icons[i]));
        }
        return sunSignModelList;
    }

    @Override
    public String toString() {
        return "SunSignModel{" +
                "sunId=" + sunId +
                ", sunSignName='" + sunSignName + '\'' +
                ", romanName='" + romanName + '\'' +
                ", sunIcon=" + sunIcon +
                '}';
    }
}
