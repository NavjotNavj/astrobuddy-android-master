package in.appnow.astrobuddy.conversation_module.activity.dagger;

import dagger.Component;
import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.dagger.component.AppComponent;


@ConversationScope
@Component(modules = ConversationModule.class,dependencies = AppComponent.class)
public interface ConversationComponent {
    void inject(ConversationActivity conversationActivity);
}
