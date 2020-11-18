package com.github.polydome.journow.domain.controller;

import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.exception.TrackerNotRunningException;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;

import java.time.Clock;
import java.util.Optional;
import java.util.function.Supplier;

public class Tracker {
    private final TaskRepository taskRepository;
    private final TrackerDataStorage dataStorage;
    private final Clock clock;
    private final SessionRepository sessionRepository;

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

        dataStorage.save(new TrackerData(taskId, clock.instant()));
    }

    public void stop() {
        Optional<TrackerData> data = dataStorage.read();

        if (data.isEmpty())
            throw new TrackerNotRunningException();
        else {
            Optional<Task> task = taskRepository.findById(data.get().getTaskId());

            sessionRepository.insert(new Session(data.get().getStartTime(), clock.instant(), task.orElse(null)));
            dataStorage.clear();
        }
    }
}
