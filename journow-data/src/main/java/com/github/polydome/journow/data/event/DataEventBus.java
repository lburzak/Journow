package com.github.polydome.journow.data.event;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class DataEventBus {
    private final Subject<DataEvent> taskSubject = PublishSubject.create();
    private final Subject<DataEvent> sessionSubject = PublishSubject.create();

    public void pushTaskEvent(DataEvent event) {
        taskSubject.onNext(event);
    }

    public void pushSessionEvent(DataEvent event) {
        sessionSubject.onNext(event);
    }

    public Observable<DataEvent> taskEvents() {
        return taskSubject.toSerialized();
    }

    public Observable<DataEvent> sessionEvents() {
        return sessionSubject.toSerialized();
    }
}
