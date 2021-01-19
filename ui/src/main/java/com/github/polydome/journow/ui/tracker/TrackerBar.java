package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.ui.listmodel.ProjectListModel;
import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TrackerBar extends JPanel {
    private final TrackerViewModel viewModel;
    private final JComboBox<String> projectSelector;

    private final JLabel elapsedTimeCounter = new JLabel();
    private final JLabel taskTitleLabel = new JLabel();
    private final JTextField taskTitleInput = new JTextField();
    private final JButton stopTrackerButton = new JButton("Stop");
    private final JButton startTrackerButton = new JButton("Start");
    private final JButton logButton = new JButton("Log");

    @Inject
    public TrackerBar(TrackerViewModel viewModel, ProjectListModel projectListModel) {
        super();
        this.viewModel = viewModel;
        projectSelector = new JComboBox<>(projectListModel);

        setLayout(new GridBagLayout());

        elapsedTimeCounter.setFont(new Font("Roboto", Font.PLAIN, 24));
        stopTrackerButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                viewModel.endSession();
            }
        });

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
                    projectSelector.setVisible(!it);
                    startTrackerButton.setVisible(!it);
                    logButton.setVisible(!it);
                });

        setVisible(true);
    }

    private void inflateLayout() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 10, 0, 0);
        constraints.weightx = 0.1;
        add(elapsedTimeCounter, constraints);

        constraints.weightx = 0.8;
        add(taskTitleLabel, constraints);
        add(taskTitleInput, constraints);
        constraints.gridy = 1;
        add(projectSelector, constraints);

        constraints.insets = new Insets(0, 10, 0, 10);
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
                viewModel.startSession(taskTitleInput.getText());
            }
        });
    }
}
