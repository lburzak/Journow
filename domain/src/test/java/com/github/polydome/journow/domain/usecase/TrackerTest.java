package com.github.polydome.journow.domain.usecase;

import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.model.TrackerData;
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
    Tracker SUT = new Tracker(taskRepository, trackerDataStorage, clock);

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
}