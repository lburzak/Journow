package com.github.polydome.journow.data.repository;

import com.github.polydome.journow.data.Database;
import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.data.event.DataEventBus;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.repository.SessionRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.github.polydome.journow.data.repository.ResultSetUtil.parseSession;

public class SessionRepositoryImpl implements SessionRepository {
    private final Database database;
    private final DataEventBus dataEventBus;

    private PreparedStatement insertSession;
    private PreparedStatement insertNewSession;
    private PreparedStatement selectAll;

    public SessionRepositoryImpl(Database database, DataEventBus dataEventBus) {
        this.database = database;
        this.dataEventBus = dataEventBus;
    }

    @Override
    public void insert(Session session) {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            long insertedId = 0;

            if (session.getId() == 0) {
                if (insertNewSession == null)
                    insertNewSession = database.getConnection().prepareStatement("insert into session (task_id, start_date, end_date) values (?, ?, ?)");

                insertNewSession.setLong(1, session.getTask().getId());
                insertNewSession.setTimestamp(2, Timestamp.from(session.getStartedAt()));
                insertNewSession.setTimestamp(3, Timestamp.from(session.getEndedAt()));
                insertNewSession.execute();

                try (ResultSet generatedKeys = insertNewSession.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        insertedId = generatedKeys.getLong(1);
                }
            } else {
                if (insertSession == null)
                    insertSession = database.getConnection().prepareStatement("insert into session (session_id, task_id, start_date, end_date) values (?, ?, ?, ?)");

                insertSession.setLong(1, session.getId());
                insertSession.setLong(2, session.getTask().getId());
                insertSession.setTimestamp(3, Timestamp.from(session.getStartedAt()));
                insertSession.setTimestamp(4, Timestamp.from(session.getEndedAt()));

                if (insertSession.executeUpdate() > 0) {
                    insertedId = session.getId();
                }
            }

            if (insertedId > 0)
                dataEventBus.pushSessionEvent(DataEvent.insertOne(insertedId));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Session> findAll() {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            if (selectAll == null)
                selectAll = database.getConnection().prepareStatement("select *" +
                        "from session\n" +
                        "         left join task on session.task_id = task.task_id\n" +
                        "         left join project p on task.project_id = p.project_id");

            try (ResultSet rows = selectAll.executeQuery()) {
                ArrayList<Session> sessions = new ArrayList<>();

                while (rows.next()) {
                    sessions.add(parseSession(rows));
                }

                return sessions;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
