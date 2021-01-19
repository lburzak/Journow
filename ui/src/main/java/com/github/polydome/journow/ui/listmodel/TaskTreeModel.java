package com.github.polydome.journow.ui.listmodel;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;

import javax.inject.Inject;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TaskTreeModel extends DefaultTreeModel {

    // A bit hacky way to retrieve selected item
    private final Map<String, Object> labelItemMap = new HashMap<>();

    @Inject
    public TaskTreeModel(TaskRepository taskRepository) {
        super(new DefaultMutableTreeNode());
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
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(formatProject(project));

            for (var task : projectTaskMap.get(project)) {
                node.add(new DefaultMutableTreeNode(formatTask(task)));
            }

            root.add(node);
        }

        for (var task : orphanTasks) {
            root.add(new DefaultMutableTreeNode(formatTask(task)));
        }
    }

    private String formatProject(Project project) {
        String label = "<html><b><font color=#AAAAAA>#" + project.getId() +" <font color=#000000>" + project.getName() + "</html>";
        labelItemMap.put(label, project);
        return label;
    }

    private String formatTask(Task task) {
        String label = "<html><font color=#AAAAAA>#" + task.getId() +" <font color=#000000>" + task.getTitle() + "</html>";
        labelItemMap.put(label, task);
        return label;
    }

    public Object getObjectByLabel(String label) {
        return labelItemMap.get(label);
    }
}
