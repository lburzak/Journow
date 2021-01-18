package com.github.polydome.journow.data.event;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class DataEventBus {
    private final Subject<DataEvent> taskSubject = PublishSubject.create();
    private final Subject<DataEvent> sessionSubject = PublishSubject.create();
    private final Subject<DataEvent> projectSubject = PublishSubject.create();

    public void pushTaskEvent(DataEvent event) {
        taskSubject.onNext(event);
    }

    public void pushSessionEvent(DataEvent event) {
        sessionSubject.onNext(event);
    }

    public void pushProjectEvent(DataEvent event) {
        projectSubject.onNext(event);
    }

    public Observable<DataEvent> taskEvents() {
        return taskSubject.toSerialized();
    }

    public Observable<DataEvent> sessionEvents() {
        return sessionSubject.toSerialized();
    }

    public Observable<DataEvent> projectEvents() {
        return projectSubject.toSerialized();
    }
}
