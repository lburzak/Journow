package com.github.polydome.journow.ui.tracker;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TrackerWindow extends JFrame {
    private final BorderLayout layout = new BorderLayout();

    @Inject
    public TrackerWindow(ListsPager listsPager, TrackerBar trackerBar) {
        createWindow();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));

        JSeparator separator = new JSeparator();
        separator.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        bottomPanel.add(separator);
        bottomPanel.add(trackerBar);

        mainPanel.add(listsPager, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        add(mainPanel);

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
