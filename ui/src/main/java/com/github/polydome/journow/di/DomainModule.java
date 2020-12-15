package com.github.polydome.journow.di;

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
    TaskRepository taskRepository() {
        return new TaskRepository() {
            @Override
            public Optional<Task> findById(long taskId) {
                return Optional.of(new Task(12, "Sample task"));
            }
        };
    }

    @Provides
    SessionRepository sessionRepository() {
        return new SessionRepository() {
            @Override
            public void insert(Session session) {
                System.out.println("Inserted session: " + session.toString());
            }
        };
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
        tracker.start(12);
        return tracker;
    }
}
