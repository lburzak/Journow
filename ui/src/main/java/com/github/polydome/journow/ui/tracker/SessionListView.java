package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.ui.listmodel.SessionTableModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class SessionListView extends JPanel {
    @Inject
    public SessionListView(SessionTableModel model) {
        setLayout(new GridBagLayout());
        var constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        JTable table = new JTable(model);

        add(table, constraints);
        add(new JScrollPane(table), constraints);
    }
}
