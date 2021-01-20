package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.repository.ProjectRepository;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class ProjectPreviewPane extends JPanel implements EntityEditorForm {
    private final JTextField nameField = new JTextField();
    private final ProjectRepository projectRepository;

    private long previewedProjectId = -1;

    @Inject
    public ProjectPreviewPane(PreviewModel model, ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        JLabel nameLabel = new JLabel("Name");

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.weightx = 0.2;
        add(nameLabel, constraints);

        constraints.weightx = 0.8;
        add(nameField, constraints);

        model.previewObjects().subscribe(obj -> {
            if (obj instanceof Project) {
                setProject((Project) obj);
            }
        });
    }

    public void setProject(Project project) {
        previewedProjectId = project.getId();
        nameField.setText(project.getName());
    }

    @Override
    public void submit() {
        projectRepository.update(new Project(previewedProjectId, nameField.getText()));
    }
}
