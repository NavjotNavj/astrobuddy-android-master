package in.appnow.astrobuddy.ui.activities.chat_feedback.dagger;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.activities.chat_feedback.ChatFeedbackActivity;

/**
 * Created by sonu on 17:26, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
@ChatFeedbackScope
@Component(modules = ChatFeedbackModule.class, dependencies = AppComponent.class)
public interface ChatFeedbackComponent {
    void inject(ChatFeedbackActivity activity);
}
