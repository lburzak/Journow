package com.github.polydome.journow.data;

import com.github.polydome.journow.data.database.MemoryDatabase;
import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ProjectRepositoryImplTest {
    Database database = new MemoryDatabase();
    ProjectRepository SUT = new ProjectRepositoryImpl(database, dataEventBus);

    @Test
    void findAll_databaseNotReady_throwsException() {
        Exception exception = assertThrows(IllegalStateException.class, () ->
                SUT.findAll()
        );

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void insert_databaseNotReady_throwsException() {
        Exception exception = assertThrows(IllegalStateException.class, () ->
                SUT.insert(createProject(0))
        );

        assertThat(exception.getMessage(), equalTo("Database is not ready"));
    }

    @Test
    void insert_projectWithExplicitIdNotExists_insertsProject() {
        Project project = createProject();

        database.init();
        SUT.insert(project);

        assertProjectInDB(project);
    }

    @Test
    void insert_projectWithImplicitId_insertsNewProject() {
        Project project = createProject(0);

        database.init();
        SUT.insert(project);

        assertProjectInDB(new Project(1, project.getName()));
    }

    @Test
    void findAll_noProjects_returnsEmptyList() {
        database.init();
        var projects = SUT.findAll();

        assertThat(projects, hasSize(0));
    }

    @Test
    void findAll_projectsInDB_returnsAllProjects() {
        Project project1 = createProject(0);
        Project project2 = createProject(0);

        database.init();
        SUT.insert(project1);
        SUT.insert(project2);

        var projects = SUT.findAll();

        assertThat(projects, hasItems(
                createProject(1),
                createProject(2)
        ));
    }

    @Test
    void insert_projectWithExplicitIdNotExists_returnsCreatedProject() {
        database.init();

        Project project = createProject(12);

        var createdProject = SUT.insert(project);

        assertThat(createdProject, equalTo(project));
    }

    @Test
    void insert_projectWithImplicitId_returnsCreatedProject() {
        database.init();

        Project project = createProject(0);

        var createdProject = SUT.insert(project);

        assertThat(createdProject, equalTo(new Project(1, project.getName())));
    }

    Project createProject() {
        return createProject(72);
    }

    Project createProject(long id) {
        return new Project(id, "test project");
    }

    void assertProjectInDB(Project project) {
        try (var rs = database.getConnection().createStatement()
                .executeQuery("select * from project where project_id = " + project.getId())) {
            assertThat(rs.next(), equalTo(true));
            assertThat(rs.getString("project_name"), equalTo(project.getName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}