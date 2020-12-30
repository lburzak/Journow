package com.github.polydome.journow.data;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
    Connection getConnection() throws SQLException;
    boolean isReady();
}
