package com.github.polydome.journow.di;

import com.github.polydome.journow.data.Database;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    PresentationComponent createPresentationComponent();
    Database database();
}
