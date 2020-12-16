package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TrackerIndex extends JPanel {
    private final SpringLayout layout = new SpringLayout();
    JLabel elapsedTimeCounter = new JLabel();
    JLabel taskTitleLabel = new JLabel();

    private void inflateLayout() {
        layout.putConstraint(SpringLayout.SOUTH, elapsedTimeCounter, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.WEST, elapsedTimeCounter, 5, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, taskTitleLabel, 0, SpringLayout.VERTICAL_CENTER, elapsedTimeCounter);
        layout.putConstraint(SpringLayout.WEST, taskTitleLabel, 12, SpringLayout.EAST, elapsedTimeCounter);
        layout.putConstraint(SpringLayout.EAST, taskTitleLabel, 5, SpringLayout.EAST, this);
    }

    public void onCreate() {
        inflateLayout();
    }

    @Inject
    public TrackerIndex(TrackerViewModel viewModel) {
        super();
        int margin = 10;
        Border emptyBorder = BorderFactory.createEmptyBorder(margin, margin, margin, margin);
        setBorder(emptyBorder);

        setLayout(layout);

        elapsedTimeCounter.setFont(new Font("Roboto", Font.PLAIN, 24));

        add(elapsedTimeCounter);
        add(taskTitleLabel);

        viewModel.getTimer()
                .subscribe(elapsedTimeCounter::setText);

        viewModel.getTaskTitle()
                .subscribe(taskTitleLabel::setText);

        setVisible(true);
    }
}
