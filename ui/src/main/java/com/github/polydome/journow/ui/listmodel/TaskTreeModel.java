package com.github.polydome.journow.ui.listmodel;

import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TaskTreeModel extends DefaultTreeModel {

    private final TaskRepository taskRepository;

    private final Subject<Boolean> _reloads = BehaviorSubject.create();

    @Inject
    public TaskTreeModel(TaskRepository taskRepository, @Named("TaskDataEvents") Observable<DataEvent> events) {
        super(new DefaultMutableTreeNode());
        this.taskRepository = taskRepository;
        populate();
        _reloads.onNext(true);

        events.subscribe(ev -> {
            ((DefaultMutableTreeNode) getRoot()).removeAllChildren();
            populate();
            reload();
            _reloads.onNext(true);
        });
    }

    public Observable<Boolean> reloads() {
        return _reloads.toSerialized();
    }

    private void populate() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        Map<Project, List<Task>> projectTaskMap = new HashMap<>();
        List<Task> tasks = taskRepository.findAll();

        for (var task : tasks) {
            if (!projectTaskMap.containsKey(task.getProject()))
                projectTaskMap.put(task.getProject(), new LinkedList<>());

            projectTaskMap.get(task.getProject()).add(task);
        }

        var orphanTasks = projectTaskMap.remove(null);

        for (var project : projectTaskMap.keySet()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(project);

            for (var task : projectTaskMap.get(project)) {
                node.add(new DefaultMutableTreeNode(task));
            }

            root.add(node);
        }

        if (orphanTasks != null) {
            for (var task : orphanTasks) {
                root.add(new DefaultMutableTreeNode(task));
            }
        }
    }
}
