package in.appnow.astrobuddy.dagger.component;

import dagger.Component;
import in.appnow.astrobuddy.conversation_module.background_service.ConversationIntentService;
import in.appnow.astrobuddy.fcm.MyFirebaseMessagingService;
import in.appnow.astrobuddy.services.MyIntentService;
import in.appnow.astrobuddy.dagger.module.MyServiceModule;
import in.appnow.astrobuddy.dagger.scope.MyServiceScope;

@MyServiceScope
@Component(modules = MyServiceModule.class, dependencies = AppComponent.class)
public interface MyServiceComponent {
    void inject(MyIntentService myIntentService);

   // void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(ConversationIntentService conversationIntentService);
}
