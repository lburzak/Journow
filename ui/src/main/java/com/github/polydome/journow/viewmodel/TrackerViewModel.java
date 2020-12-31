package com.github.polydome.journow.viewmodel;

import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.model.Task;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;

public class TrackerViewModel {

    private final Tracker tracker;
    private final Observable<Long> updateInterval;

    @Inject
    public TrackerViewModel(Tracker tracker, @Named("TimerUpdateInterval") Observable<Long> updateInterval) {
        this.tracker = tracker;
        this.updateInterval = updateInterval;
    }

    public Observable<Boolean> hasOngoingSession() {
        return tracker.isRunning();
    }

    public Observable<String> getTimer() {
        return tracker.timeElapsed(updateInterval).map(this::millisToReadableDuration);
    }

    public Observable<String> getTaskTitle() {
        return tracker.currentTask().map(Task::getTitle);
    }

    public String millisToReadableDuration(long millis) {
        Duration duration = Duration.ofMillis(millis);

        return String.format("%02d:%02d", duration.toMinutesPart(), duration.toSecondsPart());
    }
}
