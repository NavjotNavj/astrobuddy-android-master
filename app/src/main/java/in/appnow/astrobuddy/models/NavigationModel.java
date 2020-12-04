package in.appnow.astrobuddy.models;

/**
 * Created by sonu on 12:31, 27/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class NavigationModel {
    private int icon;
    private String label,badge;
    private boolean isSwitch;

    public NavigationModel() {
    }

    public NavigationModel(int icon, String label, String badge,boolean isSwitch) {
        this.icon = icon;
        this.label = label;
        this.badge = badge;
        this.isSwitch=isSwitch;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
