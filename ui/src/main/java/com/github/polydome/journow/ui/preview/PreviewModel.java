package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Task;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class PreviewModel {
    private final Subject<Object> _previewObjects = PublishSubject.create();

    public Observable<Object> previewObjects() {
        return _previewObjects.toSerialized();
    }

    public void previewTask(Task task) {
        _previewObjects.onNext(task);
    }
}
