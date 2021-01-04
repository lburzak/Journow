package com.github.polydome.journow.di;

import com.github.polydome.journow.data.Database;
import com.github.polydome.journow.data.database.LocalDatabase;
import com.github.polydome.journow.data.event.DataEventBus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    Database database() {
        return new LocalDatabase();
    }

    @Provides
    @Singleton
    DataEventBus dataEventBus() {
        return new DataEventBus();
    }
}
