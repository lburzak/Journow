package com.github.polydome.journow.data;

import com.github.polydome.journow.data.database.MemoryDatabase;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
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

    @Test
    void insert_sessionIdIs0_insertsNewSession() {
        Session session = createSession(0);

        database.init();
        SUT.insert(session);

        try (PreparedStatement stmt = database.getConnection().prepareStatement("select session_id from session where session_id == 1")) {
            stmt.execute();

            assertThat(stmt.getResultSet().next(), equalTo(true));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAll_databaseNotReady_throwsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> SUT.findAll());

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void findAll_tasksInDatabase_returnsTasksList() throws SQLException {
        database.init();

        Task task = createTask();

        Instant start = Instant.ofEpochMilli(125500000);
        Instant end = start.plusMillis(15000);

        var stmt = database.getConnection().prepareStatement("insert into task (task_id, title) values (?, ?)");
        stmt.setLong(1, task.getId());
        stmt.setString(2, task.getTitle());
        stmt.execute();

        Session session1 = new Session(0, start, end, task);
        Session session2 = new Session(0, start, end, task);

        SUT.insert(session1);
        SUT.insert(session2);

        List<Session> sessions = SUT.findAll();

        assertThat(sessions.size(), equalTo(2));
        assertThat(sessions, hasItems(
                new Session(1, start, end, task),
                new Session(2, start, end, task)
        ));
    }

    Task createTask() {
        return new Task(2, "Test task");
    }

    Session createSession() {
        return createSession(12);
    }

    Session createSession(long id) {
        Instant startDate = Instant.ofEpochMilli(680002334);
        Instant endDate = startDate.plusSeconds(10);
        Task task = createTask();

        return new Session(id, startDate, endDate, task);
    }
}