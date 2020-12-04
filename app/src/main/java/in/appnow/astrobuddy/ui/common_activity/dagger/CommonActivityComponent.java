package in.appnow.astrobuddy.ui.common_activity.dagger;

import dagger.Component;
import in.appnow.astrobuddy.conversation_module.activity.dagger.ConversationScope;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.common_activity.CommonActivity;
import in.appnow.astrobuddy.ui.fragments.HelpFragment;

/**
 * Created by sonu on 16:08, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
@ConversationScope
@Component(modules = CommonActivityModule.class, dependencies = AppComponent.class)
public interface CommonActivityComponent {
    void inject(CommonActivity commonActivity);

    void inject(HelpFragment helpFragment);
}
