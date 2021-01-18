package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.ui.tab.TaskTab;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class ListsPager extends JPanel {
    @Inject
    public ListsPager(SessionListView sessionListView, TaskTab taskTab) {
        JTabbedPane mainPane = new JTabbedPane();

        setLayout(new GridBagLayout());
        var constraints = new GridBagConstraints();

        mainPane.addTab("Sessions", sessionListView);
        mainPane.addTab("Tasks", taskTab);

        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.insets = new Insets(10, 10, 0, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        add(mainPane, constraints);
    }
}
