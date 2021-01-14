package com.github.polydome.journow.di;

import com.github.polydome.journow.data.Database;
import com.github.polydome.journow.data.database.LocalDatabase;
import com.github.polydome.journow.data.event.DataEventBus;
import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.time.Clock;

@Module(includes = DomainModule.class)
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

    @Provides
    @Singleton
    Tracker tracker(TaskRepository taskRepository, TrackerDataStorage trackerDataStorage, Clock clock, SessionRepository sessionRepository) {
        return new Tracker(
                taskRepository, trackerDataStorage, clock, sessionRepository
        );
    }
}
