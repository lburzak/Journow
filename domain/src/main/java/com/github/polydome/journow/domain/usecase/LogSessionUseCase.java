package com.github.polydome.journow.domain.usecase;

import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;

import java.time.Instant;
import java.util.Optional;

public class LogSessionUseCase {
    private final TaskRepository taskRepository;
    private final SessionRepository sessionRepository;

    public LogSessionUseCase(TaskRepository taskRepository, SessionRepository sessionRepository) {
        this.taskRepository = taskRepository;
        this.sessionRepository = sessionRepository;
    }

    public void execute(Instant startedAt, Instant endedAt, long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);


        if (task.isEmpty())
            throw new NoSuchTaskException(taskId);

        if (startedAt.isAfter(endedAt))
            throw new IllegalArgumentException("End date precedes start date");

        Session session = new Session(startedAt, endedAt, task.get());

        sessionRepository.insert(session);
    }
}
