package in.appnow.astrobuddy.conversation_module.activity.dagger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.conversation_module.activity.mvp.ConversationActivityView;
import in.appnow.astrobuddy.conversation_module.activity.mvp.ConversationModel;
import in.appnow.astrobuddy.conversation_module.activity.mvp.ConversationPresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;


@Module
public class ConversationModule {
    private final AppCompatActivity appCompatActivity;

    public ConversationModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @ConversationScope
    public ConversationActivityView view() {
        return new ConversationActivityView(appCompatActivity);
    }

    @Provides
    @ConversationScope
    public ConversationModel conversationModel(APIInterface blurtApiInterface, ViewModelProvider.Factory viewModeFactory, ABDatabase abDatabase) {
        return new ConversationModel(appCompatActivity, blurtApiInterface,viewModeFactory, abDatabase);
    }

    @Provides
    @ConversationScope
    public ConversationPresenter conversationPresenter(ConversationActivityView view, ConversationModel model, PreferenceManger blurtPreferenceManger, ABDatabase abDatabase) {
        return new ConversationPresenter(view, model, blurtPreferenceManger,abDatabase);
    }
}
