package in.appnow.astrobuddy.ui.fragments.referral;

/**
 * Created by sonu on 15:52, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ReferralPojo {
    private String packageName;
    private int drawableIcon;

    public ReferralPojo(String packageName, int drawableIcon) {
        this.packageName = packageName;
        this.drawableIcon = drawableIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getDrawableIcon() {
        return drawableIcon;
    }


    @Override
    public String toString() {
        return "ReferralPojo{" +
                "packageName='" + packageName + '\'' +
                ", drawableIcon=" + drawableIcon +
                '}';
    }
}
