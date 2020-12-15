package com.github.polydome.journow.ui;

import com.github.polydome.journow.di.ApplicationComponent;

import javax.swing.*;

public class App {
    private final ApplicationComponent applicationComponent;

    public App(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public void run() {
        // Enable Anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings", "lcd");

        try {
            // FIXME Implement fallback L&F
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        applicationComponent.createPresentationComponent()
                .trackerWindow()
                .showWindow();
    }
}
