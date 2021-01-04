package com.github.polydome.journow.data;

import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.data.event.DataEventBus;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {
    private final Database database;
    private final DataEventBus dataEventBus;

    private PreparedStatement insertNewTask;
    private PreparedStatement insertTask;
    private PreparedStatement countTasks;
    private PreparedStatement findAll;

    public TaskRepositoryImpl(Database database, DataEventBus dataEventBus) {
        this.database = database;
        this.dataEventBus = dataEventBus;
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

    @Override
    public Task insert(Task task) {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            if (task.getId() == 0) {
                if (insertNewTask == null)
                    insertNewTask = getConnection().prepareStatement("insert into task (title) values (?)");

                insertNewTask.setString(1, task.getTitle());
                insertNewTask.execute();

                try (ResultSet generatedKeys = insertNewTask.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        dataEventBus.pushTaskEvent(DataEvent.insertOne(id));
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

    @Override
    public int count() {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            if (countTasks == null)
                countTasks = getConnection().prepareStatement("select count(*) from task");

            try (ResultSet rs = countTasks.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
                else
                    return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<Task> findAll() {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            if (findAll == null)
                findAll = getConnection().prepareStatement("select task_id, title from task");

            try (var rs = findAll.executeQuery()) {
                ArrayList<Task> tasks = new ArrayList<>();

                while (rs.next()) {
                    tasks.add(
                            new Task(rs.getLong(1), rs.getString(2))
                    );
                }

                return tasks;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return List.of();
    }

    private Connection getConnection() throws SQLException {
        return database.getConnection();
    }
}
