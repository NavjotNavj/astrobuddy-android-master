package in.appnow.astrobuddy.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by sonu on 21:09, 02/01/19
 * Copyright (c) 2019 . All rights reserved.
 */
@IgnoreExtraProperties
public class ServerStatusModel {
    @PropertyName("restrict_access")
    public boolean isRestrictAccess;
    @PropertyName("restrict_msg")
    public String message;

    public ServerStatusModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public boolean isRestrictAccess() {
        return isRestrictAccess;
    }

    public void setRestrictAccess(boolean restrictAccess) {
        isRestrictAccess = restrictAccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ServerStatusModel{" +
                "isRestrictAccess=" + isRestrictAccess +
                ", message='" + message + '\'' +
                '}';
    }
}
