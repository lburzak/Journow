package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TrackerWindow extends JFrame {
    private final BorderLayout layout = new BorderLayout();

    @Inject
    public TrackerWindow(TrackerViewModel trackerViewModel) {
        createWindow();
        createContentView(trackerViewModel);
    }

    private void createWindow() {
        setTitle("Journow - Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(layout);

        setLocationRelativeTo(null);

        setSize(800, 400);
    }

    private void createContentView(TrackerViewModel viewModel) {
        TrackerBar mainPanel = new TrackerBar(viewModel);
        getContentPane().add(mainPanel, BorderLayout.PAGE_END);

        mainPanel.onCreate();
    }

    public void showWindow() {
        setVisible(true);
    }

}
