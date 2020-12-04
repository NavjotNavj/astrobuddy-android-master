package in.appnow.astrobuddy.ui.activities.intro;

/**
 * Created by sonu on 18:13, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class IntroModel {
    private int introImage;
    private String introTitle,introDesc;

    public IntroModel(int introImage, String introTitle, String introDesc) {
        this.introImage = introImage;
        this.introTitle = introTitle;
        this.introDesc = introDesc;
    }

    public int getIntroImage() {
        return introImage;
    }

    public String getIntroTitle() {
        return introTitle;
    }

    public String getIntroDesc() {
        return introDesc;
    }
}
