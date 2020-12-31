package com.github.polydome.journow.data;

import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.repository.SessionRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionRepositoryImpl implements SessionRepository {
    private final Database database;

    private PreparedStatement insertSession;
    private PreparedStatement insertNewSession;

    public SessionRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public void insert(Session session) {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            if (session.getId() == 0) {
                if (insertNewSession == null)
                    insertNewSession = database.getConnection().prepareStatement("insert into session (task_id, start_date, end_date) values (?, ?, ?)");

                insertNewSession.setLong(1, session.getTask().getId());
                insertNewSession.setTimestamp(2, Timestamp.from(session.getStartedAt()));
                insertNewSession.setTimestamp(3, Timestamp.from(session.getEndedAt()));

                insertNewSession.execute();
            } else {
                if (insertSession == null)
                    insertSession = database.getConnection().prepareStatement("insert into session (session_id, task_id, start_date, end_date) values (?, ?, ?, ?)");

                insertSession.setLong(1, session.getId());
                insertSession.setLong(2, session.getTask().getId());
                insertSession.setTimestamp(3, Timestamp.from(session.getStartedAt()));
                insertSession.setTimestamp(4, Timestamp.from(session.getEndedAt()));

                insertSession.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
