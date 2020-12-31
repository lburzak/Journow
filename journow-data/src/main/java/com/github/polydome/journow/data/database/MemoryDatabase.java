package com.github.polydome.journow.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MemoryDatabase extends SingleConnectionDatabase {
    @Override
    protected Connection newConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }
}
