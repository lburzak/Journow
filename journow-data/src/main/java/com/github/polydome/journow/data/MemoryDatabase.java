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

    public void init() throws SQLException {
        connection = newConnection();
        try (var statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS task (" +
                "task_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title VARCHAR(120) NOT NULL" +
                ");")
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initialized = true;
    }
}
