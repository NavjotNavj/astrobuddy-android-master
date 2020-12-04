package in.appnow.astrobuddy.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by sonu on 17:46, 13/02/19
 * Copyright (c) 2019 . All rights reserved.
 */
@IgnoreExtraProperties
public class ABConfigModel {

    @PropertyName("server_status")
    public ServerStatusModel serverStatusModel;
    @PropertyName("app_upgrade")
    public AppUpgradeModel appUpgradeModel;

    public ABConfigModel() {
    }

    public ServerStatusModel getServerStatusModel() {
        return serverStatusModel;
    }

    public void setServerStatusModel(ServerStatusModel serverStatusModel) {
        this.serverStatusModel = serverStatusModel;
    }

    public AppUpgradeModel getAppUpgradeModel() {
        return appUpgradeModel;
    }

    public void setAppUpgradeModel(AppUpgradeModel appUpgradeModel) {
        this.appUpgradeModel = appUpgradeModel;
    }

    @Override
    public String toString() {
        return "ABConfigModel{" +
                "serverStatusModel=" + serverStatusModel +
                ", appUpgradeModel=" + appUpgradeModel +
                '}';
    }
}
