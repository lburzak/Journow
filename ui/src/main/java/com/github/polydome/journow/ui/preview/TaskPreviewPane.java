package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.ui.listmodel.ProjectListModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskPreviewPane extends JPanel implements EntityEditorForm {
    private final JTextField titleField = new JTextField();
    private final JComboBox<String> projectField = new JComboBox<>();
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectListModel projectListModel;

    private long previewedTaskId = -1;

    @Inject
    public TaskPreviewPane(PreviewModel model, TaskRepository taskRepository, ProjectRepository projectRepository, ProjectListModel projectListModel) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.projectListModel = projectListModel;
        JLabel titleLabel = new JLabel("Title");
        JLabel projectLabel = new JLabel("Project");

        projectField.setEditable(true);
        projectField.setModel(projectListModel);

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

        if (projectField.getSelectedItem() == null || projectField.getSelectedItem().equals(""))
            project = null;
        else if (selectedProjectIndex == -1)
            project = projectRepository.insert(new Project(0, (String) projectField.getSelectedItem()));
        else
            project = projectListModel.getProjectAt(selectedProjectIndex);

        taskRepository.update(new Task(previewedTaskId, titleField.getText(), project));
    }
}
