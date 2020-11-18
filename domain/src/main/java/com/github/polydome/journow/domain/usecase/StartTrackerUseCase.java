package com.github.polydome.journow.domain.usecase;

import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;

import java.time.Clock;
import java.util.Optional;

public class StartTrackerUseCase {
    private final TaskRepository taskRepository;
    private final TrackerDataStorage dataStorage;
    private final Clock clock;

    public StartTrackerUseCase(TaskRepository taskRepository, TrackerDataStorage dataStorage, Clock clock) {
        this.taskRepository = taskRepository;
        this.dataStorage = dataStorage;
        this.clock = clock;
    }

    public void execute(long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isEmpty())
            throw new NoSuchTaskException(taskId);

        dataStorage.save(new TrackerData(taskId, clock.instant()));
    }
}
