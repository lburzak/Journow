package com.github.polydome.journow.ui;

import com.github.polydome.journow.viewmodel.TrackerViewModel;

import javax.swing.*;

public class TrackerWindow extends JFrame {

    public TrackerWindow() {
        super("Journow - Tracker");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setSize(800, 64 + 20);
        JPanel mainPanel = new TrackerIndex(new TrackerViewModel());
        setContentPane(mainPanel);

        setVisible(true);
    }
}
