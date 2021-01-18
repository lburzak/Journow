package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TaskPreviewPane extends JPanel implements EntityEditorForm {
    private final JTextField titleField = new JTextField("Hello there");
    private final JComboBox<String> projectField = new JComboBox<>();
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private long previewedTaskId = -1;
    List<Project> projects = List.of();

    @Inject
    public TaskPreviewPane(PreviewModel model, TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        JLabel titleLabel = new JLabel("Title");
        JLabel projectLabel = new JLabel("Project");

        projectField.setEditable(true);

        populateProjectsList();

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.weightx = 0.2;
        add(titleLabel, constraints);

        constraints.weightx = 0.8;
        add(titleField, constraints);

        constraints.gridy = 1;
        constraints.weightx = 0.2;
        add(projectLabel, constraints);

        constraints.weightx = 0.8;
        add(projectField, constraints);

        model.previewObjects().subscribe(obj -> {
            if (obj instanceof Task) {
                setTask((Task) obj);
            }
            else {
                throw new UnsupportedOperationException("Unsupported preview object");
            }
        });
    }

    public void setTask(Task task) {
        previewedTaskId = task.getId();
        titleField.setText(task.getTitle());
        Project project = task.getProject();
        if (project == null)
            projectField.setSelectedIndex(0);
        else
            projectField.setSelectedItem(project.getName());
    }

    @Override
    public void submit() {
        Project project;
        int selectedProjectIndex = projectField.getSelectedIndex();

        if (selectedProjectIndex == 0)
            project = null;
        else if (selectedProjectIndex == -1)
            project = projectRepository.insert(new Project(0, (String) projectField.getSelectedItem()));
        else
            project = projects.get(selectedProjectIndex);

        taskRepository.update(new Task(previewedTaskId, titleField.getText(), project));
    }

    private void populateProjectsList() {
        projects = projectRepository.findAll();

        projectField.addItem("<No project>");
        for (final var project : projects)
            projectField.addItem(project.getName());
    }
}
