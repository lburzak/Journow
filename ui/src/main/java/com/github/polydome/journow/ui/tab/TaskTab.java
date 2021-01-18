package com.github.polydome.journow.ui.tab;

import com.github.polydome.journow.ui.tracker.PreviewView;
import com.github.polydome.journow.ui.tracker.TaskListView;

import javax.inject.Inject;
import javax.swing.*;

public class TaskTab extends JPanel {
    @Inject
    public TaskTab(TaskListView taskListView, PreviewView previewView) {
        add(taskListView);
        add(previewView);
    }
}
