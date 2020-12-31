package com.github.polydome.journow.domain.controller;

import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.exception.TrackerNotRunningException;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;

public class Tracker {
    private final TaskRepository taskRepository;
    private final TrackerDataStorage dataStorage;
    private final Clock clock;
    private final SessionRepository sessionRepository;
    private final Subject<Task> _currentTask = BehaviorSubject.create();
    private final Subject<Boolean> _isRunning = BehaviorSubject.createDefault(false);

    public Tracker(TaskRepository taskRepository, TrackerDataStorage dataStorage, Clock clock, SessionRepository sessionRepository) {
        this.taskRepository = taskRepository;
        this.dataStorage = dataStorage;
        this.clock = clock;
        this.sessionRepository = sessionRepository;
    }

    public void start(long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isEmpty())
            throw new NoSuchTaskException(taskId);

        _currentTask.onNext(task.get());

        dataStorage.save(new TrackerData(taskId, clock.instant()));

        _isRunning.onNext(true);
    }

    public void stop() {
        Optional<TrackerData> data = dataStorage.read();

        if (data.isEmpty())
            throw new TrackerNotRunningException();
        else {
            Optional<Task> task = taskRepository.findById(data.get().getTaskId());

            sessionRepository.insert(new Session(0, data.get().getStartTime(), clock.instant(), task.orElse(null)));
            dataStorage.clear();

            _isRunning.onNext(false);
        }
    }

    public Observable<Boolean> isRunning() {
        return _isRunning.toSerialized();
    }

    public Observable<Task> currentTask() {
        return _currentTask.toSerialized();
    }

    public Observable<Long> timeElapsed(Observable<Long> interval) {
        return interval
                .flatMapSingle(tick -> calculateTimeElapsed())
                .map(Duration::toMillis);
    }

    private Single<Duration> calculateTimeElapsed() {
        return Single.create(emitter -> {
            Optional<TrackerData> data = dataStorage.read();
            if (data.isPresent()) {
                emitter.onSuccess(Duration.between(clock.instant(), data.get().getStartTime()).abs());
            } else {
                emitter.onError(new TrackerNotRunningException());
            }
        });
    }
}
