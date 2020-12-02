package com.github.polydome.journow.ui;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.swing.*;

public class TrackerIndex extends JPanel {
    private final SpringLayout layout = new SpringLayout();
    JLabel elapsedTimeCounter = new JLabel();
    JLabel taskTitleLabel = new JLabel();

    private void layoutComponents() {
        layout.putConstraint(SpringLayout.NORTH, elapsedTimeCounter, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, elapsedTimeCounter, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.WEST, elapsedTimeCounter, 5, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, taskTitleLabel, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, taskTitleLabel, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.WEST, taskTitleLabel, 5, SpringLayout.EAST, elapsedTimeCounter);
        layout.putConstraint(SpringLayout.EAST, taskTitleLabel, 5, SpringLayout.EAST, this);
    }

    public TrackerIndex(TrackerViewModel viewModel) {
        super();
        int margin = 10;
        setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        setLayout(layout);
        layoutComponents();
        add(elapsedTimeCounter);
        add(taskTitleLabel);

        viewModel.getTimer()
                .subscribe(elapsedTimeCounter::setText);

        viewModel.getTaskTitle()
                .subscribe(taskTitleLabel::setText);

        setVisible(true);
    }
}
