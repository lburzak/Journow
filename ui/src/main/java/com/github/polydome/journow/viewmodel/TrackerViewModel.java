package com.github.polydome.journow.viewmodel;

import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class TrackerViewModel {

    @Inject
    public TrackerViewModel() {}

    public Observable<String> getTimer() {
        return Observable.just("13:37");
    }

    public Observable<String> getTaskTitle() {
        return Observable.just("Reading JSwing documentation");
    }
}
