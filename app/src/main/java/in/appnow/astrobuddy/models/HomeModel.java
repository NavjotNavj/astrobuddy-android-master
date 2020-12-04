package in.appnow.astrobuddy.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class HomeModel {

    public int[] homeIcons;
    public List<String> homeTitles;

    public int[] getHomeIcons() {
        return homeIcons;
    }

    public void setHomeIcons(int[] homeIcons) {
        this.homeIcons = homeIcons;
    }

    public List<String> getHomeTitles() {
        return homeTitles;
    }

    public void setHomeTitles(List<String> homeTitles) {
        this.homeTitles = homeTitles;
    }
}
