package com.github.polydome.journow.ui.listmodel;

import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;
import java.util.List;

public class ProjectListModel extends DefaultComboBoxModel<String> {
    private final List<Project> projects;

    @Inject
    public ProjectListModel(ProjectRepository projectRepository, @Named("ProjectDataEvents") Observable<DataEvent> events) {
        projects = projectRepository.findAll();

        events.subscribe(ev -> {
            List<Project> freshTasks = projectRepository.findAll();
            projects.clear();
            projects.addAll(freshTasks);

            switch (ev.getType()) {
                case INSERT:
                    var insertedProject = projects.stream().filter(proj -> proj.getId() == ev.getIdStart()).findFirst();
                    if (insertedProject.isPresent()) {
                        var index = projects.indexOf(insertedProject.get());
                        fireIntervalAdded(this, index, index);
                    } else {
                        System.err.println("Inserted project not found");
                    }
                    break;
                case CHANGE:
                    var updatedProject = projects.stream().filter(task -> task.getId() == ev.getIdStart()).findFirst();
                    if (updatedProject.isPresent()) {
                        var index = projects.indexOf(updatedProject.get());
                        fireContentsChanged(this, index, index);
                    } else {
                        System.err.println("Updated project not found");
                    }
                    break;
            }
        });
    }

    @Override
    public int getSize() {
        return projects.size() + 1;
    }

    @Override
    public String getElementAt(int i) {
        if (i == 0)
            return null;
        else
            return projects.get(i - 1).getName();
    }

    public Project getProjectAt(int i) {
        if (i == 0)
            return null;
        else
            return projects.get(i - 1);
    }
}
