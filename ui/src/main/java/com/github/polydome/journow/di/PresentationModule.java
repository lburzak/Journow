package com.github.polydome.journow.di;

import com.github.polydome.journow.viewmodel.TrackerViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class PresentationModule {
    @Provides
    public TrackerViewModel trackerViewModel() {
        return new TrackerViewModel();
    }
}
