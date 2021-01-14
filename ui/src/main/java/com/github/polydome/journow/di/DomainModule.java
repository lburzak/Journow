package com.github.polydome.journow.di;

import com.github.polydome.journow.data.Database;
import com.github.polydome.journow.data.SessionRepositoryImpl;
import com.github.polydome.journow.data.TaskRepositoryImpl;
import com.github.polydome.journow.data.event.DataEventBus;
import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.time.Clock;
import java.util.Optional;

@Module
public class DomainModule {
    @Provides
    TaskRepository taskRepository(Database database, DataEventBus dataEventBus) {
        return new TaskRepositoryImpl(database, dataEventBus);
    }

    @Provides
    SessionRepository sessionRepository(Database database, DataEventBus dataEventBus) {
        return new SessionRepositoryImpl(database, dataEventBus);
    }

    @Provides
    TrackerDataStorage trackerDataStorage() {
        return new TrackerDataStorage() {
            private TrackerData trackerData;

            @Override
            public void save(TrackerData data) {
                trackerData = data;
            }

            @Override
            public Optional<TrackerData> read() {
                if (trackerData != null)
                    return Optional.of(trackerData);
                else
                    return Optional.empty();
            }

            @Override
            public void clear() {
                trackerData = null;
            }
        };
    }

    @Provides
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
