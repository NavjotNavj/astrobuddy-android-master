package in.appnow.astrobuddy.interfaces;

/**
 * Created by sonu on 17:34, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public interface OnToolbarListener {

    public void showToolBar(boolean show);

    public void onBackButtonChange(boolean isEnable);

    public void onToolbarTitleChange(String title);

    public void onBackButtonPress();

    public void showHideBuyCreditsButton(int visibility);

    public void showToolbarBackTutorialPrompt(int iconName);

    public void hideBuyTopicTooltip();

    public void hideBottomBar(boolean isHide);

}
