package com.github.polydome.journow.domain.usecase;

import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.service.TrackerDataStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TrackerTest {
    TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    TrackerDataStorage trackerDataStorage = Mockito.mock(TrackerDataStorage.class);
    Clock clock = Mockito.mock(Clock.class);
    SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    Tracker SUT = new Tracker(taskRepository, trackerDataStorage, clock, sessionRepository);

    @Test
    public void start_taskNotExists_throwsNoSuchTaskException() {
        long TASK_ID = 15;
        Mockito.when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        NoSuchTaskException exception = assertThrows(NoSuchTaskException.class, () ->
                SUT.start(TASK_ID));

        assertThat(exception.getMessage(), equalTo(String.format("Task identified with [id=%d] does not exist", TASK_ID)));
    }

    @Test
    public void start_taskExists_savesData() {
        // given
        Instant now = Instant.ofEpochMilli(12000000);
        long TASK_ID = 15;
        when(clock.instant()).thenReturn(now);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(new Task(TASK_ID, "test task")));

        // when
        SUT.start(TASK_ID);

        // then
        ArgumentCaptor<TrackerData> dataCpt = ArgumentCaptor.forClass(TrackerData.class);
        verify(trackerDataStorage).save(dataCpt.capture());

        TrackerData actual = dataCpt.getValue();
        assertThat(actual.getStartTime(), equalTo(now));
        assertThat(actual.getTaskId(), equalTo(TASK_ID));
    }

    @Test
    public void stop_trackerRunning_savesSession() {
        // given
        Instant start = Instant.ofEpochMilli(600000);
        Instant now = Instant.ofEpochMilli(12000000);
        Task task = new Task(15, "test task");
        when(clock.instant()).thenReturn(now);
        when(trackerDataStorage.read()).thenReturn(new TrackerData(task.getId(), start));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // when
        SUT.stop();

        // then
        ArgumentCaptor<Session> sessionCpt = ArgumentCaptor.forClass(Session.class);

        verify(sessionRepository).insert(sessionCpt.capture());

        Session actual = sessionCpt.getValue();
        assertThat(actual.getStartedAt(), equalTo(start));
        assertThat(actual.getEndedAt(), equalTo(now));
        assertThat(actual.getTask(), equalTo(task));
    }

    @Test
    public void stop_trackerRunning_clearsData() {
        Task task = new Task(15, "test task");
        when(trackerDataStorage.read()).thenReturn(new TrackerData(task.getId(), Instant.ofEpochMilli(233)));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        SUT.stop();

        verify(trackerDataStorage).clear();
    }

    @Test
    public void stop_storedTaskNotExists_saveAnonymousSession() {
        // given
        Instant start = Instant.ofEpochMilli(600000);
        Instant now = Instant.ofEpochMilli(12000000);
        long TASK_ID = 15;
        when(clock.instant()).thenReturn(now);
        when(trackerDataStorage.read()).thenReturn(new TrackerData(TASK_ID, start));
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        // when
        SUT.stop();

        // then
        ArgumentCaptor<Session> sessionCpt = ArgumentCaptor.forClass(Session.class);

        verify(sessionRepository).insert(sessionCpt.capture());

        Session actual = sessionCpt.getValue();
        assertThat(actual.getStartedAt(), equalTo(start));
        assertThat(actual.getEndedAt(), equalTo(now));
        assertThat(actual.getTask(), equalTo(null));
    }
}