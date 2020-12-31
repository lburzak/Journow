package com.github.polydome.journow.di;

import com.github.polydome.journow.data.Database;
import com.github.polydome.journow.data.MemoryDatabase;
import com.github.polydome.journow.data.SessionRepositoryImpl;
import com.github.polydome.journow.data.TaskRepositoryImpl;
import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;
import dagger.Module;
import dagger.Provides;

import java.time.Clock;
import java.util.Optional;

@Module
public class DomainModule {
    @Provides
    TaskRepository taskRepository(Database database) {
        return new TaskRepositoryImpl(database);
    }

    @Provides
    SessionRepository sessionRepository(Database database) {
        return new SessionRepositoryImpl(database);
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

    @Provides
    Tracker tracker(TaskRepository taskRepository, TrackerDataStorage trackerDataStorage, Clock clock, SessionRepository sessionRepository) {
        Tracker tracker = new Tracker(
                taskRepository, trackerDataStorage, clock, sessionRepository
        );
        return tracker;
    }

    @Provides
    Database database() {
        return new MemoryDatabase();
    }
}
