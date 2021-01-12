package com.github.polydome.journow.viewmodel;

import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.ui.tracker.common.FormatUtils;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;

public class TrackerViewModel {

    private final Tracker tracker;
    private final Observable<Long> updateInterval;
    private final TaskRepository taskRepository;

    @Inject
    public TrackerViewModel(Tracker tracker, @Named("TimerUpdateInterval") Observable<Long> updateInterval, TaskRepository taskRepository) {
        this.tracker = tracker;
        this.updateInterval = updateInterval;
        this.taskRepository = taskRepository;
    }

    public Observable<Boolean> hasOngoingSession() {
        return tracker.isRunning();
    }

    public Observable<String> getTimer() {
        return hasOngoingSession().switchMap(ongoingSession -> {
            if (ongoingSession)
                return tracker.timeElapsed(updateInterval);
            else
                return Observable.just(0L);
        }).map(this::millisToReadableDuration);
    }

    public Observable<String> getTaskTitle() {
        return tracker.currentTask().map(Task::getTitle);
    }

    public String millisToReadableDuration(long millis) {
        Duration duration = Duration.ofMillis(millis);
        return FormatUtils.formatDuration(duration);
    }

    public void startSession(String title) {
        Task task = taskRepository.insert(new Task(0, title));

        tracker.start(task.getId());
    }

    public void endSession() {
        tracker.stop();
    }
}
