package com.github.polydome.journow.ui.listmodel;

import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;
import java.util.List;

public class TaskListModel extends AbstractListModel<Task> {
    private final TaskRepository taskRepository;
    private final List<Task> tasks;

    @Inject
    public TaskListModel(TaskRepository taskRepository, @Named("TaskDataEvents") Observable<DataEvent> events) {
        this.taskRepository = taskRepository;
        tasks = taskRepository.findAll();

        events.subscribe(ev -> {
            List<Task> freshTasks = taskRepository.findAll();
            tasks.clear();
            tasks.addAll(freshTasks);

            switch (ev.getType()) {
                case INSERT:
                    var insertedTask = tasks.stream().filter(task -> task.getId() == ev.getIdStart()).findFirst();
                    if (insertedTask.isPresent()) {
                        var index = tasks.indexOf(insertedTask.get());
                        fireIntervalAdded(this, index, index);
                    } else {
                        System.err.println("Inserted task not found");
                    }
                    break;
                case CHANGE:
                    var updatedTask = tasks.stream().filter(task -> task.getId() == ev.getIdStart()).findFirst();
                    if (updatedTask.isPresent()) {
                        var index = tasks.indexOf(updatedTask.get());
                        fireContentsChanged(this, index, index);
                    } else {
                        System.err.println("Updated task not found");
                    }
                    break;
            }
        });
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
