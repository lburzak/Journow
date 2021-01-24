package com.github.polydome.journow.domain.repository;

import com.github.polydome.journow.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Optional<Task> findById(long taskId);
    Task insert(Task task);
    void update(Task task);
    int count();
    List<Task> findAll();
    void delete(Task task);
    long findTotalTrackedMillis(long taskId);
}
