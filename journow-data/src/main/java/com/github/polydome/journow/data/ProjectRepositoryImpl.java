package com.github.polydome.journow.data;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.repository.ProjectRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.github.polydome.journow.data.ResultSetUtil.parseProject;
import static com.github.polydome.journow.data.ResultSetUtil.parseSession;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final Database database;

    private PreparedStatement selectAll;
    private PreparedStatement insertWithId;
    private PreparedStatement insertNew;

    public ProjectRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public List<Project> findAll() {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        ArrayList<Project> projects = new ArrayList<>();

        try {
            if (selectAll == null)
                selectAll = database.getConnection().prepareStatement("select * from project");

            try (ResultSet rows = selectAll.executeQuery()) {
                while (rows.next()) {
                    projects.add(parseProject(rows));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public void insert(Project project) {
        if (!database.isReady())
            throw new IllegalStateException("Database is not ready");

        try {
            if (project.getId() > 0) {
                if (insertWithId == null)
                    insertWithId = getConnection().prepareStatement("insert into project (project_id, project_name) values (?, ?)");

                insertWithId.setLong(1, project.getId());
                insertWithId.setString(2, project.getName());

                insertWithId.execute();
            } else {
                if (insertNew == null)
                    insertNew = getConnection().prepareStatement("insert into project (project_name) values (?)");

                insertNew.setString(1, project.getName());
                insertNew.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return database.getConnection();
    }
}
