package com.github.polydome.journow.data;

import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private PreparedStatement insertNewTask;
    private PreparedStatement insertTask;

    @Override
    public Task insert(Task task) {
        try {
            if (task.getId() == 0) {
                if (insertNewTask == null)
                    insertNewTask = getConnection().prepareStatement("insert into task (title) values (?)");

                insertNewTask.setString(1, task.getTitle());
                insertNewTask.execute();

                try (ResultSet generatedKeys = insertNewTask.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        return new Task(id, task.getTitle());
                    }
                }
            } else {
                if (insertTask == null)
                    insertTask = getConnection().prepareStatement("insert into task (task_id, title) values (?, ?)");

                insertTask.setLong(1, task.getId());
                insertTask.setString(2, task.getTitle());
                insertTask.execute();

                return task;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Connection getConnection() throws SQLException {
        return database.getConnection();
    }
}
