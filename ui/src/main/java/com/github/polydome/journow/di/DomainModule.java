package com.github.polydome.journow.di;

import com.github.polydome.journow.data.Database;
import com.github.polydome.journow.data.ProjectRepositoryImpl;
import com.github.polydome.journow.data.SessionRepositoryImpl;
import com.github.polydome.journow.data.TaskRepositoryImpl;
import com.github.polydome.journow.data.event.DataEventBus;
import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;
import com.github.polydome.journow.domain.usecase.LogSessionUseCase;
import dagger.Module;
import dagger.Provides;

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
    ProjectRepository projectRepository(Database database, DataEventBus dataEventBus) {
        return new ProjectRepositoryImpl(database, dataEventBus);
    }

    @Provides
    LogSessionUseCase logSessionUseCase(TaskRepository taskRepository, SessionRepository sessionRepository) {
        return new LogSessionUseCase(taskRepository, sessionRepository);
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
