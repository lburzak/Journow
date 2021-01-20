package com.github.polydome.journow.data;

import com.github.polydome.journow.data.database.MemoryDatabase;
import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.data.event.DataEventBus;
import com.github.polydome.journow.data.repository.TaskRepositoryImpl;
import com.github.polydome.journow.domain.exception.NoSuchTaskException;
import com.github.polydome.journow.domain.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.github.polydome.journow.data.test.TaskFactory.createTask;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TaskRepositoryImplTest {
    MemoryDatabase database = new MemoryDatabase();
    DataEventBus dataEventBus = mock(DataEventBus.class);
    TaskRepositoryImpl SUT = new TaskRepositoryImpl(database, dataEventBus);

    @Test
    public void findById_taskNotExists_returnsEmpty() throws SQLException {
        database.init();
        var result = SUT.findById(12);
        assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    public void findById_taskExists_returnsTask() throws SQLException {
        Task expected = createTask();

        database.init();
        try (var stmt = database.getConnection().prepareStatement("insert into task (task_id, title) values (? ,?)")) {
            stmt.setLong(1, expected.getId());
            stmt.setString(2, expected.getTitle());
            stmt.execute();
        }

        var result = SUT.findById(expected.getId());

        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get().getId(), equalTo(expected.getId()));
        assertThat(result.get().getTitle(), equalTo(expected.getTitle()));
    }

    @Test
    void findById_databaseNotReady_throwsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> SUT.findById(12));

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void insert_taskNotExists_insertsTask() throws SQLException {
        Task task = createTask();

        database.init();
        SUT.insert(task);

        try (
                var stmt = database.getConnection()
                        .prepareStatement("select task_id, title from task where task_id = ?")
        ) {
            stmt.setLong(1, task.getId());

            try (var resultSet = stmt.executeQuery()) {
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getLong(1), equalTo(task.getId()));
                assertThat(resultSet.getString(2), equalTo(task.getTitle()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void insert_taskNotExists_returnsCreatedTask() throws SQLException {
        Task task = createTask(0);

        database.init();
        Task actual = SUT.insert(task);

        assertThat(actual.getTitle(), equalTo(task.getTitle()));
        assertThat(actual.getId(), equalTo(1L));
    }

    @Test
    void insert_databaseNotReady_throwsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class,
                () -> SUT.insert(createTask(0))
        );

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void count_databaseNotReady_throwsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> SUT.count());

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void count_noTasks_returns0() {
        database.init();

        int count = SUT.count();

        assertThat(count, equalTo(0));
    }

    @Test
    void count_tasksInDatabase_returnsTasksCount() {
        database.init();

        SUT.insert(createTask(0));
        SUT.insert(createTask(0));

        int count = SUT.count();

        assertThat(count, equalTo(2));
    }

    @Test
    void findAll_databaseNotReady_throwsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> SUT.findAll());

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void findAll_tasksInDatabase_returnsTasksList() {
        database.init();

        SUT.insert(createTask(0, "test task 1"));
        SUT.insert(createTask(0, "test task 2"));

        List<Task> tasks = SUT.findAll();

        assertThat(tasks.size(), equalTo(2));
        assertThat(tasks, hasItems(
                createTask(1, "test task 1"),
                createTask(2, "test task 2")
        ));
    }

    @Test
    void insert_taskWithoutIdInserted_dispatchesEvent() {
        database.init();
        SUT.insert(createTask(0));

        ArgumentCaptor<DataEvent> eventCpt = ArgumentCaptor.forClass(DataEvent.class);
        verify(dataEventBus, Mockito.times(1)).pushTaskEvent(eventCpt.capture());

        DataEvent actual = eventCpt.getValue();
        assertThat(actual.getType(), equalTo(DataEvent.Type.INSERT));
        assertThat(actual.getIdStart(), equalTo(1L));
        assertThat(actual.getIdStop(), equalTo(1L));
    }

    @Test
    void insert_taskWithIdInserted_dispatchesEvent() {
        database.init();
        SUT.insert(createTask(12));

        ArgumentCaptor<DataEvent> eventCpt = ArgumentCaptor.forClass(DataEvent.class);
        verify(dataEventBus, Mockito.times(1)).pushTaskEvent(eventCpt.capture());

        DataEvent actual = eventCpt.getValue();
        assertThat(actual.getType(), equalTo(DataEvent.Type.INSERT));
        assertThat(actual.getIdStart(), equalTo(12L));
        assertThat(actual.getIdStop(), equalTo(12L));
    }

    @Test
    void update_taskNotExists_throwsException() {
        database.init();

        assertThrows(NoSuchTaskException.class, () -> {
            SUT.update(createTask(2, "test task"));
        });
    }

    @Test
    void update_taskExists_updatesTask() throws SQLException {
        database.init();

        database.getConnection()
                .prepareStatement("insert into task (title) values ('test task')")
                .executeUpdate();

        SUT.update(createTask(1, "test task edited"));

        try (var rs = database.getConnection()
                .prepareStatement("select task_id, title from task where task_id = 1")
                .executeQuery()) {
            assertThat(rs.next(), equalTo(true));
            assertThat(rs.getString(2), equalTo("test task edited"));
        }
    }

    @Test
    void update_taskExists_emitsUpdateEvent() throws SQLException {
        database.init();

        database.getConnection()
                .prepareStatement("insert into task (title) values ('test task')")
                .executeUpdate();

        SUT.update(createTask(1, "test task edited"));

        var cpt = ArgumentCaptor.forClass(DataEvent.class);
        verify(dataEventBus).pushTaskEvent(cpt.capture());

        var event = cpt.getValue();
        assertThat(event.getType(), equalTo(DataEvent.Type.CHANGE));
        assertThat(event.getIdStart(), equalTo(event.getIdStop()));
        assertThat(event.getIdStart(), equalTo(1L));
    }
}