package com.github.polydome.journow.ui.tab;

import com.github.polydome.journow.ui.tracker.PreviewView;
import com.github.polydome.journow.ui.tracker.TaskListView;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskTab extends JSplitPane {
    @Inject
    public TaskTab(TaskListView taskListView, PreviewView previewView) {
        taskListView.setPreferredSize(new Dimension(300, 200));
        setLeftComponent(taskListView);
        setRightComponent(previewView);
    }
}
