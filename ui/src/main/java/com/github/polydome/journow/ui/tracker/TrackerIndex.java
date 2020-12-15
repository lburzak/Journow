package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;

public class TrackerIndex extends JPanel {
    private final SpringLayout layout = new SpringLayout();
    JLabel elapsedTimeCounter = new JLabel();
    JLabel taskTitleLabel = new JLabel();

    private void inflateLayout() {
        layout.putConstraint(SpringLayout.NORTH, elapsedTimeCounter, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, elapsedTimeCounter, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.WEST, elapsedTimeCounter, 5, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, taskTitleLabel, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, taskTitleLabel, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.WEST, taskTitleLabel, 5, SpringLayout.EAST, elapsedTimeCounter);
        layout.putConstraint(SpringLayout.EAST, taskTitleLabel, 5, SpringLayout.EAST, this);
    }

    @Inject
    public TrackerIndex(TrackerViewModel viewModel) {
        super();
        int margin = 10;
        setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        setLayout(layout);
        inflateLayout();
        add(elapsedTimeCounter);
        add(taskTitleLabel);

        viewModel.getTimer()
                .subscribe(elapsedTimeCounter::setText);

        viewModel.getTaskTitle()
                .subscribe(taskTitleLabel::setText);

        setVisible(true);
    }
}
