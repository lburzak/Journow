package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TrackerWindow extends JFrame {
    private final BorderLayout layout = new BorderLayout();

    @Inject
    public TrackerWindow(ListsPager listsPager, TrackerBar trackerBar) {
        createWindow();
        getContentPane().add(trackerBar, BorderLayout.PAGE_END);
        getContentPane().add(listsPager, BorderLayout.CENTER);

        trackerBar.onCreate();
    }

    private void createWindow() {
        setTitle("Journow - Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(layout);

        setLocationRelativeTo(null);

        setSize(800, 400);
    }

    public void showWindow() {
        setVisible(true);
    }

}
