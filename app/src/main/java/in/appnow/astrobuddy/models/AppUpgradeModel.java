package in.appnow.astrobuddy.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by sonu on 17:46, 13/02/19
 * Copyright (c) 2019 . All rights reserved.
 */
@IgnoreExtraProperties
public class AppUpgradeModel {

    @PropertyName("app_version")
    public int prodAppVersion;
    @PropertyName("force_upgrade")
    public boolean isForceUpgrade;
    @PropertyName("recommended_upgrade")
    public boolean isRecommendedUpgrade;
    @PropertyName("upgrade_message")
    public String message;

    public AppUpgradeModel() {
    }

    public int getProdAppVersion() {
        return prodAppVersion;
    }

    public void setProdAppVersion(int prodAppVersion) {
        this.prodAppVersion = prodAppVersion;
    }

    public boolean isForceUpgrade() {
        return isForceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        isForceUpgrade = forceUpgrade;
    }

    public boolean isRecommendedUpgrade() {
        return isRecommendedUpgrade;
    }

    public void setRecommendedUpgrade(boolean recommendedUpgrade) {
        isRecommendedUpgrade = recommendedUpgrade;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AppUpgradeModel{" +
                "prodAppVersion=" + prodAppVersion +
                ", isForceUpgrade=" + isForceUpgrade +
                ", isRecommendedUpgrade=" + isRecommendedUpgrade +
                ", message='" + message + '\'' +
                '}';
    }
}
