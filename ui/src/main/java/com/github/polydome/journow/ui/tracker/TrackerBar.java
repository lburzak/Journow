package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static javax.swing.SpringLayout.*;

public class TrackerBar extends JPanel {
    private final SpringLayout layout = new SpringLayout();
    JLabel elapsedTimeCounter = new JLabel();
    JLabel taskTitleLabel = new JLabel();

    private void inflateLayout() {
        layout.putConstraint(SOUTH, elapsedTimeCounter, -5, SOUTH, this);
        layout.putConstraint(WEST, elapsedTimeCounter, 5, WEST, this);
        layout.putConstraint(VERTICAL_CENTER, elapsedTimeCounter, 0, VERTICAL_CENTER, this);

        layout.putConstraint(VERTICAL_CENTER, taskTitleLabel, 0, VERTICAL_CENTER, elapsedTimeCounter);
        layout.putConstraint(HORIZONTAL_CENTER, taskTitleLabel, 0, HORIZONTAL_CENTER, this);
    }

    public void onCreate() {
        setPreferredSize(new Dimension(0, 60));
        inflateLayout();
    }

    @Inject
    public TrackerBar(TrackerViewModel viewModel) {
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
