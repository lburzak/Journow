package com.github.polydome.journow.data.repository;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetUtil {
    public static Task parseTask(ResultSet rs) throws SQLException {
        return Task.builder()
                .id(rs.getLong("task_id"))
                .title(rs.getString("title"))
                .project(parseProject(rs))
                .build();
    }

    public static Project parseProject(ResultSet rs) throws SQLException {
        long id = rs.getLong("project_id");
        if (id > 0)
            return new Project(id, rs.getString("project_name"));
        else return null;
    }

    public static Session parseSession(ResultSet rs) throws SQLException {
        return new Session(
                rs.getLong("session_id"),
                rs.getTimestamp("start_date").toInstant(),
                rs.getTimestamp("end_date").toInstant(),
                parseTask(rs)
        );
    }
}
