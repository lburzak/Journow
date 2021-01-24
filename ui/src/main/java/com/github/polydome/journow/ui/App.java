package com.github.polydome.journow.ui;

import com.alee.laf.WebLookAndFeel;
import com.alee.skin.dark.WebDarkSkin;
import com.alee.skin.light.WebLightSkin;
import com.github.polydome.journow.di.ApplicationComponent;

import javax.swing.*;

public class App {
    private final ApplicationComponent applicationComponent;

    public App(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public void run() {
        applicationComponent.database().init();

        // Enable Anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings", "lcd");

        WebLookAndFeel.install();

        applicationComponent.createPresentationComponent()
                .trackerWindow()
                .showWindow();
    }
}
