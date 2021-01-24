package com.github.polydome.journow.ui.tracker;

import com.alee.laf.text.WebTextField;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.ui.control.ProjectSelector;
import com.github.polydome.journow.ui.dialog.LogDialogFactory;
import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TrackerBar extends JPanel {
    private final TrackerViewModel viewModel;
    private final ProjectSelector projectSelector;
    private final LogDialogFactory logDialogFactory;

    private final JLabel elapsedTimeCounter = new JLabel();
    private final JLabel taskTitleLabel = new JLabel();
    private final WebTextField taskTitleInput = new WebTextField();
    private final JButton stopTrackerButton = new JButton("Stop");
    private final JButton startTrackerButton = new JButton("Start");
    private final JButton logButton = new JButton("Log");

    @Inject
    public TrackerBar(TrackerViewModel viewModel, ProjectSelector projectSelector, LogDialogFactory logDialogFactory) {
        super();
        this.viewModel = viewModel;
        this.projectSelector = projectSelector;
        this.logDialogFactory = logDialogFactory;
        taskTitleInput.setInputPrompt("Task name");

        setLayout(new GridBagLayout());

        elapsedTimeCounter.setFont(new Font("Roboto", Font.PLAIN, 24));
        stopTrackerButton.addActionListener(a -> viewModel.endSession());
        startTrackerButton.addActionListener(a -> viewModel
                .startSession(taskTitleInput.getText(),
                    projectSelector.getSelectedProject(),
                    projectSelector.hasCustomProject())
        );

        logButton.addActionListener(a -> showLogDialog());

        setupKeys();

        viewModel.getTimer()
                .subscribe(elapsedTimeCounter::setText);

        viewModel.getTaskTitle()
                .subscribe(taskTitleLabel::setText);

        viewModel.hasOngoingSession()
                .subscribe(it -> {
                    taskTitleLabel.setVisible(it);
                    elapsedTimeCounter.setVisible(it);
                    stopTrackerButton.setVisible(it);
                    taskTitleInput.setVisible(!it);
                    this.projectSelector.setVisible(!it);
                    startTrackerButton.setVisible(!it);
                    logButton.setVisible(!it);
                });

        setVisible(true);
    }

    private void inflateLayout() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.1;
        add(elapsedTimeCounter, constraints);

        constraints.weightx = 0.8;
        add(taskTitleLabel, constraints);
        add(taskTitleInput, constraints);
        constraints.gridy = 1;
        add(projectSelector, constraints);

        constraints.gridy = 0;
        constraints.weightx = 0.1;
        add(stopTrackerButton, constraints);
        add(startTrackerButton, constraints);
        constraints.gridy = 1;
        add(logButton, constraints);
    }

    public void onCreate() {
        setPreferredSize(new Dimension(0, 60));
        inflateLayout();
    }

    private void setupKeys() {
        taskTitleInput.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "start");
        taskTitleInput.getActionMap().put("start", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                viewModel.startSession(taskTitleInput.getText(), projectSelector.getSelectedProject(), projectSelector.hasCustomProject());
            }
        });
    }

    private void showLogDialog() {
        if (taskTitleInput.getText().equals(""))
            logDialogFactory.createBlank();
        else
            logDialogFactory.createLinked(
                    new Task(0, taskTitleInput.getText(), projectSelector.getSelectedProject())
            );
    }
}
