package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Task;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class PreviewModel {
    public Observable<Object> previewObjects() {
        return Observable.just(new Task(0, "this is a preview test"));
    }

    @Inject
    public PreviewModel() {
    }
}
