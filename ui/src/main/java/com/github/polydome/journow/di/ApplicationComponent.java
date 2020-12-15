package com.github.polydome.journow.di;

import dagger.Component;

@Component
public interface ApplicationComponent {
    PresentationComponent createPresentationComponent();
}
