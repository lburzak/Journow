package com.github.polydome.journow.ui.tracker;

import javax.swing.*;

public class ListsPager extends JTabbedPane {
    public ListsPager() {
        addTab("Sessions", new SessionListView());
        addTab("Tasks", new TaskListView());
    }
}
