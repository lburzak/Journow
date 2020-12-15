package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;

public class TrackerWindow extends JFrame {

    @Inject
    public TrackerWindow(TrackerViewModel trackerViewModel) {
        createWindow();
        createContentView(trackerViewModel);
    }

    private void createWindow() {
        setTitle("Journow - Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setSize(800, 64 + 20);
    }

    private void createContentView(TrackerViewModel viewModel) {
        JPanel mainPanel = new TrackerIndex(viewModel);
        setContentPane(mainPanel);
    }

    public void showWindow() {
        setVisible(true);
    }

}
