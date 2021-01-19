package com.github.polydome.journow.ui.dialog;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.polydome.journow.ui.control.ProjectSelector;
import com.github.polydome.journow.ui.listmodel.ProjectListModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class LogDialog extends JDialog {
    private final JTextField titleField = new JTextField();
    private final ProjectSelector projectField;
    private final DateTimePicker startDatePicker = new DateTimePicker();
    private final DateTimePicker endDatePicker = new DateTimePicker();

    @Inject
    public LogDialog(ProjectListModel projectListModel) {
        this.projectField = new ProjectSelector(projectListModel);

        setTitle("Journow - Log session");
        setLocationRelativeTo(null);

        setSize(800, 100);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 0.3;
        c.gridy = 0;
        add(new JLabel("Task title"), c);
        c.gridy++;
        add(new JLabel("Project"), c);
        c.gridy++;
        add(new JLabel("Start date"), c);
        c.gridy++;
        add(new JLabel("End date"), c);

        c.weightx = 0.7;
        c.gridy = 0;
        c.gridx = 1;
        add(titleField, c);
        c.gridy++;
        add(projectField, c);
        c.gridy++;
        add(startDatePicker, c);
        c.gridy++;
        add(endDatePicker, c);

        c.weightx = 0.3;
        c.gridy++;
        add(new JButton("Save"), c);

        startDatePicker.getDatePicker().setDateToToday();
        startDatePicker.getTimePicker().setTimeToNow();
        endDatePicker.getDatePicker().setDateToToday();
        endDatePicker.getTimePicker().setTimeToNow();

        pack();

        setVisible(true);
    }
}
