package com.github.polydome.journow.ui.listmodel;

import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;

import javax.inject.Inject;
import javax.swing.*;
import java.util.List;

public class TaskListModel extends AbstractListModel<Task> {
    private final TaskRepository taskRepository;
    private final List<Task> tasks;

    @Inject
    public TaskListModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        tasks = taskRepository.findAll();
    }

    @Override
    public int getSize() {
        return tasks.size();
    }

    @Override
    public Task getElementAt(int i) {
        return tasks.get(i);
    }
}
