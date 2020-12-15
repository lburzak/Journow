package com.github.polydome.journow.di;

import com.github.polydome.journow.ui.tracker.TrackerWindow;
import dagger.Subcomponent;

@Subcomponent
public interface PresentationComponent {
    TrackerWindow trackerWindow();
}
