package in.appnow.astrobuddy.dao.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import in.appnow.astrobuddy.dagger.scope.AppScope;
import in.appnow.astrobuddy.dao.repository.conversation.ConversationLocalRepository;
import in.appnow.astrobuddy.dao.repository.conversation.ConversationRemoteRepository;


@AppScope
public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private final ConversationLocalRepository conversationLocalRepository;
    private final ConversationRemoteRepository conversationRemoteRepository;

    @Inject
    public CustomViewModelFactory(ConversationLocalRepository conversationLocalRepository, ConversationRemoteRepository conversationRemoteRepository) {
        this.conversationLocalRepository = conversationLocalRepository;
        this.conversationRemoteRepository = conversationRemoteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ConversationViewModel.class)) {
            return (T) new ConversationViewModel(conversationLocalRepository,conversationRemoteRepository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
