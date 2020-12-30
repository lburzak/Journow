package com.github.polydome.journow.data;

import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {
    private final Database database;

    public TaskRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public Optional<Task> findById(long taskId) {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try (final var getTaskById =
                getConnection().prepareStatement("select * from task where task_id = ?")) {
            getTaskById.setLong(1, taskId);

            try (final var taskResultSet = getTaskById.executeQuery()) {
                if (!taskResultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Task(
                        taskResultSet.getLong("task_id"),
                        taskResultSet.getString("title")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Connection getConnection() throws SQLException {
        return database.getConnection();
    }
}
