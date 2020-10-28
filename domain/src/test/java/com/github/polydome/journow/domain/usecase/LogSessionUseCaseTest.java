package com.github.polydome.journow.domain.usecase;

import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class LogSessionUseCaseTest {
    TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    LogSessionUseCase SUT = new LogSessionUseCase(taskRepository, sessionRepository);

    @BeforeEach
    public void clearMocks() {
        Mockito.reset(taskRepository, sessionRepository);
    }

    @Test
    public void execute_taskNotExists_throwsNoSuchTaskException() {
        int TASK_ID = 15;
        Mockito.when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        NoSuchTaskException exception = assertThrows(NoSuchTaskException.class, () ->
                SUT.execute(Instant.now().minusSeconds(30), Instant.now(), TASK_ID));

        assertThat(exception.getMessage(), equalTo(String.format("Task identified with [id=%d] does not exist", TASK_ID)));
    }

    @Test
    public void execute_taskExists_insertsSession() {
        int TASK_ID = 15;
        Task task = new Task(TASK_ID, "Test task");
        Instant startedAt = Instant.now().minusSeconds(30);
        Instant endedAt = Instant.now();

        Mockito.when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(task));

        SUT.execute(startedAt, endedAt, TASK_ID);

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

        verify(sessionRepository).insert(sessionCaptor.capture());

        assertThat(sessionCaptor.getValue().getTask(), equalTo(task));
        assertThat(sessionCaptor.getValue().getStartedAt(), equalTo(startedAt));
        assertThat(sessionCaptor.getValue().getEndedAt(), equalTo(endedAt));
    }

    @Test
    public void execute_endDatePrecedesStartDate_throwsIllegalArgumentException() {
        when(taskRepository.findById(15))
                .thenReturn(Optional.of(new Task(15, "test")));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                SUT.execute(Instant.now(), Instant.now().minusSeconds(50), 15));

        assertThat(exception.getMessage(), equalTo("End date precedes start date"));
    }
}