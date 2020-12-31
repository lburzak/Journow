package com.github.polydome.journow.data;

import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SessionRepositoryImplTest {
    MemoryDatabase database = new MemoryDatabase();
    SessionRepository SUT = new SessionRepositoryImpl(database);

    @Test
    void findById_databaseNotReady_throwsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () ->
                SUT.insert(createSession())
        );

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void insert_sessionNotExists_insertsSession() throws SQLException {
        Session session = createSession();

        database.init();
        SUT.insert(session);

        try (
            var stmt = database.getConnection()
                .prepareStatement("select start_date, end_date, task_id from session where session_id = ?")
        ) {
            stmt.setLong(1, session.getId());

            try (var resultSet = stmt.executeQuery()) {
                assertThat(resultSet.next(), equalTo(true));
                assertThat(resultSet.getTimestamp(1), equalTo(Timestamp.from(session.getStartedAt())));
                assertThat(resultSet.getTimestamp(2), equalTo(Timestamp.from(session.getEndedAt())));
                assertThat(resultSet.getLong(3), equalTo(session.getTask().getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Task createTask() {
        return new Task(2, "Test task");
    }

    Session createSession() {
        Instant startDate = Instant.ofEpochMilli(680002334);
        Instant endDate = startDate.plusSeconds(10);
        Task task = createTask();

        return new Session(12, startDate, endDate, task);
    }


}