package com.github.polydome.journow.data;

import com.github.polydome.journow.data.database.MemoryDatabase;
import com.github.polydome.journow.domain.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TaskRepositoryImplTest {
    MemoryDatabase database = new MemoryDatabase();
    TaskRepositoryImpl SUT = new TaskRepositoryImpl(database);

    @Test
    public void findById_taskNotExists_returnsEmpty() throws SQLException {
        database.init();
        var result = SUT.findById(12);
        assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    public void findById_taskExists_returnsTask() throws SQLException {
        Task expected = new Task(12, "Test task");

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
        Task task = new Task(2, "test task");

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
        Task task = new Task(0, "test task");

        database.init();
        Task actual = SUT.insert(task);

        assertThat(actual.getTitle(), equalTo(task.getTitle()));
        assertThat(actual.getId(), equalTo(1L));
    }

    @Test
    void insert_databaseNotReady_throwsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> SUT.insert(new Task(0, "test task")));

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

        SUT.insert(new Task(0, "test task 1"));
        SUT.insert(new Task(0, "test task 2"));

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

        SUT.insert(new Task(0, "test task 1"));
        SUT.insert(new Task(0, "test task 2"));

        List<Task> tasks = SUT.findAll();

        assertThat(tasks.size(), equalTo(2));
        assertThat(tasks, hasItems(
                new Task(1, "test task 1"),
                new Task(2, "test task 2")
        ));
    }
}