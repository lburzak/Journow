package com.github.polydome.journow.ui.popup;

import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.model.Task;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskPopupMenu extends JPopupMenu {
    private Task selectedTask;

    @Inject
    public TaskPopupMenu(Tracker tracker) {
        super("Task");
        JMenuItem startTrackingItem = new JMenuItem("Start tracking");
        startTrackingItem.addActionListener(a -> tracker.start(selectedTask.getId()));
        add(startTrackingItem);
    }

    public void show(Component invoker, int x, int y, Task task) {
        selectedTask = task;
        super.show(invoker, x, y);
    }
}
