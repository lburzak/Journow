package com.github.polydome.journow.ui.control;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.ui.listmodel.ProjectListModel;

import javax.inject.Inject;
import javax.swing.*;

public class ProjectSelector extends JComboBox<String> {
    private final ProjectListModel model;

    @Inject
    public ProjectSelector(ProjectListModel model) {
        this.model = model;
        setModel(model);
        setEditable(true);
        setToolTipText("Project name");
    }

    public boolean hasCustomProject() {
        return getSelectedIndex() == -1;
    }

    public boolean hasProjectSelected() {
        return getSelectedIndex() != 0 && getSelectedItem() != null && !getSelectedItem().equals("");
    }

    public Project getSelectedProject() {
        Project project;
        int selectedProjectIndex = getSelectedIndex();

        if (getSelectedItem() == null || getSelectedItem().equals(""))
            project = null;
        else if (selectedProjectIndex == -1)
            project = new Project(0, (String) getSelectedItem());
        else
            project = model.getProjectAt(selectedProjectIndex);

        return project;
    }
}
