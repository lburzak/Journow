package com.github.polydome.journow.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MemoryDatabase implements Database {
    private boolean initialized = false;
    private Connection connection = null;

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean isReady() {
        return initialized;
    }

    private Connection newConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }

    @Override
    public void init() {
        try {
            connection = newConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (var statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS task (" +
                "task_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title VARCHAR(120) NOT NULL" +
                ");")
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (var statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS session (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "task_id INTEGER NOT NULL," +
                "start_date TIMESTAMP NOT NULL," +
                "end_date TIMESTAMP NOT NULL," +
                "FOREIGN KEY (task_id) REFERENCES task(task_id)" +
                ");")
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initialized = true;
    }
}
