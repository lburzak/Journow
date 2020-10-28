package com.github.polydome.journow.domain.repository;

import com.github.polydome.journow.domain.model.Task;

import java.util.Optional;

public interface TaskRepository {
    Optional<Task> findById(long taskId);
}
