package com.github.polydome.journow.ui.tracker;

import javax.inject.Inject;
import javax.swing.*;

public class ListsPager extends JTabbedPane {
    @Inject
    public ListsPager(SessionListView sessionListView, TaskListView taskListView) {
        addTab("Sessions", sessionListView);
        addTab("Tasks", taskListView);
    }
}
