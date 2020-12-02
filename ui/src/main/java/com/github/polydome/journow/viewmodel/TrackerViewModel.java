package com.github.polydome.journow.viewmodel;

import io.reactivex.rxjava3.core.Observable;

public class TrackerViewModel {
    public Observable<String> getTimer() {
        return Observable.just("13:37");
    }

    public Observable<String> getTaskTitle() {
        return Observable.just("Reading JSwing documentation");
    }
}
