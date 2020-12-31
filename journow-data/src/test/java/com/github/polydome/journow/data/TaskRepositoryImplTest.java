package com.github.polydome.journow.data;

import com.github.polydome.journow.domain.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}