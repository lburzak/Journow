package com.github.polydome.journow.ui.dialog;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.ui.control.ProjectSelector;
import com.github.polydome.journow.ui.listmodel.ProjectListModel;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneOffset;

public class LogDialog extends JDialog {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SessionRepository sessionRepository;

    private final JTextField titleField = new JTextField();
    private final ProjectSelector projectField;
    private final DateTimePicker startDatePicker = new DateTimePicker();
    private final DateTimePicker endDatePicker = new DateTimePicker();

    public LogDialog(ProjectRepository projectRepository,
                     TaskRepository taskRepository,
                     SessionRepository sessionRepository,
                     ProjectListModel projectListModel,
                     Task task) {
        this(projectRepository, taskRepository, sessionRepository, projectListModel);

        titleField.setText(task.getTitle());
        titleField.setEnabled(false);

        projectField.setSelectedItem(task.getProject().getName());
        projectField.setEnabled(false);
    }

    public LogDialog(ProjectRepository projectRepository,
                     TaskRepository taskRepository,
                     SessionRepository sessionRepository,
                     ProjectListModel projectListModel) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.sessionRepository = sessionRepository;
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
        JButton submitButton = new JButton("Save");
        add(submitButton, c);
        submitButton.addActionListener(a -> submit());

        startDatePicker.getDatePicker().setDateToToday();
        startDatePicker.getTimePicker().setTimeToNow();
        endDatePicker.getDatePicker().setDateToToday();
        endDatePicker.getTimePicker().setTimeToNow();

        pack();

        setVisible(true);
    }

    private void submit() {
        Project project = projectField.getSelectedProject();
        if (projectField.hasCustomProject())
            project = projectRepository.insert(project);

        Task task = taskRepository.insert(new Task(0, titleField.getText(), project));

        // TODO: Use use case instead
        sessionRepository.insert(new Session(0,
                getDateTimeInstant(startDatePicker),
                getDateTimeInstant(endDatePicker),
                task));

        dispose();
    }

    private Instant getDateTimeInstant(DateTimePicker picker) {
        return picker.getDateTimePermissive().toInstant(ZoneOffset.UTC);
    }
}
