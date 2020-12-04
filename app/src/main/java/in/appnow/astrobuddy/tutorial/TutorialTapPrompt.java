package in.appnow.astrobuddy.tutorial;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.view.View;

import in.appnow.astrobuddy.R;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.FullscreenPromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Created by sonu on 12:25, 28/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TutorialTapPrompt {

    private static MaterialTapTargetPrompt materialTapTargetPrompt;

    public static void showNoAutoDismiss(AppCompatActivity activity, View target, String title, String description, MaterialTapTargetPrompt.PromptStateChangeListener promptStateChangeListener) {

        new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(target)
                .setPrimaryText(title)
                .setSecondaryText(description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener(promptStateChangeListener)
                .show();
    }

    public static void showNoAutoDismissWithFocalRadius(AppCompatActivity activity, View target, String title, String description, float focalRadius, MaterialTapTargetPrompt.PromptStateChangeListener promptStateChangeListener) {
        new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(target)
                .setPrimaryText(title)
                .setSecondaryText(description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setFocalRadius(focalRadius)
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener(promptStateChangeListener)
                .show();
    }

    public static void showFullScreenRectPrompt(AppCompatActivity appCompatActivity, View target, String title, String description, MaterialTapTargetPrompt.PromptStateChangeListener promptStateChangeListener) {
       new MaterialTapTargetPrompt.Builder(appCompatActivity)
                .setTarget(target)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setPrimaryText(title)
                .setSecondaryText(description)
                .setPromptBackground(new FullscreenPromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(promptStateChangeListener)
                .show();
    }
}
