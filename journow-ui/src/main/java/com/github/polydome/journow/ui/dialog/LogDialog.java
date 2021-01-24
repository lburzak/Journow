package com.github.polydome.journow.ui.dialog;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.usecase.LogSessionUseCase;
import com.github.polydome.journow.ui.control.ProjectSelector;
import com.github.polydome.journow.ui.listmodel.ProjectListModel;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

public class LogDialog extends JDialog {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final LogSessionUseCase logSessionUseCase;

    private final JTextField titleField = new JTextField();
    private final ProjectSelector projectField;
    private final DateTimePicker startDatePicker = new DateTimePicker();
    private final DateTimePicker endDatePicker = new DateTimePicker();

    private Optional<Task> lockedTask = Optional.empty();

    public LogDialog(ProjectRepository projectRepository,
                     TaskRepository taskRepository,
                     LogSessionUseCase logSessionUseCase,
                     ProjectListModel projectListModel,
                     Task task) {
        this(projectRepository, taskRepository, logSessionUseCase, projectListModel);
        lockedTask = Optional.of(task);

        titleField.setText(task.getTitle());
        titleField.setEnabled(false);

        if (task.getProject() != null)
            projectField.setSelectedItem(task.getProject().getName());
        projectField.setEnabled(false);
    }

    public LogDialog(ProjectRepository projectRepository,
                     TaskRepository taskRepository,
                     LogSessionUseCase logSessionUseCase,
                     ProjectListModel projectListModel) {
        this.logSessionUseCase = logSessionUseCase;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectField = new ProjectSelector(projectListModel);

        setTitle("Journow - Log session");
        setLocationRelativeTo(null);

        setSize(800, 100);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 0.3;
        c.gridy = 0;
        pane.add(new JLabel("Task title"), c);
        c.gridy++;
        pane.add(new JLabel("Project"), c);
        c.gridy++;
        pane.add(new JLabel("Start date"), c);
        c.gridy++;
        pane.add(new JLabel("End date"), c);

        c.weightx = 0.7;
        c.gridy = 0;
        c.gridx = 1;
        pane.add(titleField, c);
        c.gridy++;
        pane.add(projectField, c);
        c.gridy++;
        pane.add(startDatePicker, c);
        c.gridy++;
        pane.add(endDatePicker, c);

        c.weightx = 0.3;
        c.gridy++;
        JButton submitButton = new JButton("Save");
        pane.add(submitButton, c);
        submitButton.addActionListener(a -> submit());

        startDatePicker.getTimePicker().getSettings().setFormatForDisplayTime("HH:mm");
        startDatePicker.getDatePicker().setDateToToday();
        startDatePicker.getTimePicker().setTimeToNow();
        endDatePicker.getTimePicker().getSettings().setFormatForDisplayTime("HH:mm");
        endDatePicker.getDatePicker().setDateToToday();
        endDatePicker.getTimePicker().setTimeToNow();

        add(pane);
        pack();

        setVisible(true);
    }

    private void submit() {
        Project project = projectField.getSelectedProject();
        if (projectField.hasCustomProject() && project != null)
            project = projectRepository.insert(project);

        Task task;
        if (lockedTask.isPresent() && lockedTask.get().getId() > 0)
            task = lockedTask.get();
        else
            task = taskRepository.insert(new Task(0, titleField.getText(), project));

        logSessionUseCase.execute(
                getDateTimeInstant(startDatePicker),
                getDateTimeInstant(endDatePicker),
                task.getId()
        );

        dispose();
    }

    private Instant getDateTimeInstant(DateTimePicker picker) {
        return picker.getDateTimePermissive().toInstant(ZoneOffset.UTC);
    }
}
