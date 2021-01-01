package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.ui.listmodel.TaskListModel;

import javax.inject.Inject;
import javax.swing.*;

public class TaskListView extends JPanel {
    @Inject
    public TaskListView(TaskListModel model) {
        JList<Task> list = new JList<>(model);
        list.setFixedCellHeight(40);
        list.setFixedCellWidth(280);
        add(list);
    }
}
