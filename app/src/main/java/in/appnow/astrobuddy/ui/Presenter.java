package in.appnow.astrobuddy.ui;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 17:19, 18/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class Presenter {
    private final Observable<String> credentialsSampleObservable;
    private final Observable<String> credentialsSwitchMapObservable;
    private final Observable<String> credentialsWithLatestFromObservable;

    private final BehaviorSubject<String> emailSubject = BehaviorSubject.create();
    private final BehaviorSubject<String> passwordSubject = BehaviorSubject.create();
    private final PublishSubject<Void> loginClickSubject = PublishSubject.create();

    public Presenter() {

        final Observable<String> lastCredentialsObservable = Observable.combineLatest(emailSubject,
                passwordSubject, (email, password) -> email + " " + password);

        credentialsSampleObservable = lastCredentialsObservable
                .sample(loginClickSubject);

        credentialsSwitchMapObservable = loginClickSubject.
                switchMap(aVoid -> lastCredentialsObservable);


        credentialsWithLatestFromObservable = loginClickSubject
                .withLatestFrom(lastCredentialsObservable, (aVoid, credentials) -> credentials);

    }

    public Observable<String> getCredentialsWithLatestFromObservable() {
        return credentialsWithLatestFromObservable;
    }

    public Observable<String> getCredentialsSwitchMapObservable() {
        return credentialsSwitchMapObservable;
    }

    public Observable<String> getCredentialsSampleObservable() {
        return credentialsSampleObservable;
    }

    public BehaviorSubject<String> getEmailSubject() {
        return emailSubject;
    }

    public BehaviorSubject<String> getPasswordSubject() {
        return passwordSubject;
    }

    public PublishSubject<Void> getLoginClickSubject() {
        return loginClickSubject;
    }

}
