package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.exception.TrackerNotRunningException;
import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static javax.swing.SpringLayout.*;

public class TrackerBar extends JPanel {
    private final SpringLayout layout = new SpringLayout();
    JLabel elapsedTimeCounter = new JLabel();
    JLabel taskTitleLabel = new JLabel();
    JTextField taskTitleInput = new JTextField("this is an example task");
    private final TrackerViewModel viewModel;

    private void inflateLayout() {
        layout.putConstraint(SOUTH, elapsedTimeCounter, -5, SOUTH, this);
        layout.putConstraint(WEST, elapsedTimeCounter, 5, WEST, this);
        layout.putConstraint(VERTICAL_CENTER, elapsedTimeCounter, 0, VERTICAL_CENTER, this);

        layout.putConstraint(VERTICAL_CENTER, taskTitleLabel, 0, VERTICAL_CENTER, elapsedTimeCounter);
        layout.putConstraint(HORIZONTAL_CENTER, taskTitleLabel, 0, HORIZONTAL_CENTER, this);

        layout.putConstraint(EAST, taskTitleInput, 0, EAST, this);
        layout.putConstraint(WEST, taskTitleInput, 0, WEST, this);
        layout.putConstraint(SOUTH, taskTitleInput, 0, SOUTH, this);
        layout.putConstraint(NORTH, taskTitleInput, 0, NORTH, this);
    }

    public void onCreate() {
        setPreferredSize(new Dimension(0, 60));
        inflateLayout();
    }

    private void onError(Throwable e) {
        if (!(e instanceof TrackerNotRunningException))
            e.printStackTrace();
    }

    @Inject
    public TrackerBar(TrackerViewModel viewModel) {
        super();
        this.viewModel = viewModel;

        int margin = 10;
        Border emptyBorder = BorderFactory.createEmptyBorder(margin, margin, margin, margin);
        setBorder(emptyBorder);

        setLayout(layout);

        elapsedTimeCounter.setFont(new Font("Roboto", Font.PLAIN, 24));

        add(elapsedTimeCounter);
        add(taskTitleLabel);
        add(taskTitleInput);

        setupKeys();

        viewModel.getTimer()
                .subscribe(elapsedTimeCounter::setText, this::onError);

        viewModel.getTaskTitle()
                .subscribe(taskTitleLabel::setText);

        viewModel.hasOngoingSession()
                .subscribe(it -> {
                    taskTitleLabel.setVisible(it);
                    taskTitleInput.setVisible(!it);
                });

        setVisible(true);
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
