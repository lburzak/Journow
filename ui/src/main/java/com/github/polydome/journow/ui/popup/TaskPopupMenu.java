package com.github.polydome.journow.ui.popup;

import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.usecase.LogSessionUseCase;
import com.github.polydome.journow.ui.dialog.LogDialogFactory;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskPopupMenu extends JPopupMenu {
    private Task selectedTask;

    @Inject
    public TaskPopupMenu(Tracker tracker, LogDialogFactory logDialogFactory) {
        super("Task");

        JMenuItem startTrackingItem = new JMenuItem("Start tracking");
        JMenuItem logItem = new JMenuItem("Log work");

        startTrackingItem.addActionListener(a -> tracker.start(selectedTask.getId()));
        logItem.addActionListener(a -> logDialogFactory.createLinked(selectedTask));

        add(startTrackingItem);
        add(logItem);
    }

    public void show(Component invoker, int x, int y, Task task) {
        selectedTask = task;
        super.show(invoker, x, y);
    }
}
