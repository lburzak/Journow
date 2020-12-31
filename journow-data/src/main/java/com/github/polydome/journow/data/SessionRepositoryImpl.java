package com.github.polydome.journow.data;

import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.repository.SessionRepository;

import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionRepositoryImpl implements SessionRepository {
    private final Database database;

    public SessionRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public void insert(Session session) {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            try (var stmt = database.getConnection().prepareStatement("insert into session (session_id, task_id, start_date, end_date) values (?, ?, ?, ?)")) {
                stmt.setLong(1, session.getId());
                stmt.setLong(2, session.getTask().getId());

                stmt.setTimestamp(3, Timestamp.from(session.getStartedAt()));
                stmt.setTimestamp(4, Timestamp.from(session.getEndedAt()));

                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
