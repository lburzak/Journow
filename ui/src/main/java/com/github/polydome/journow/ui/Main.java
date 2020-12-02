package com.github.polydome.journow.ui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Enable Anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings", "lcd");

        try {
            // FIXME Implement fallback L&F
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        new TrackerWindow();
    }
}
