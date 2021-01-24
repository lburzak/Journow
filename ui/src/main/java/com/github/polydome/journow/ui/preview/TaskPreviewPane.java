package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.common.FormatUtils;
import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.ui.control.ProjectSelector;
import com.github.polydome.journow.ui.listmodel.ProjectListModel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskPreviewPane extends JPanel implements EntityEditorForm {
    private final JTextField titleField = new JTextField();
    private final JLabel totalTrackedTimeField = new JLabel();
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSelector projectSelector;

    private long previewedTaskId = -1;

    @Inject
    public TaskPreviewPane(PreviewModel model, TaskRepository taskRepository, ProjectSelector projectSelector, ProjectListModel projectListModel, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectSelector = projectSelector;
        this.projectRepository = projectRepository;
        JLabel titleLabel = new JLabel("Title");
        JLabel projectLabel = new JLabel("Project");
        JLabel totalTracked = new JLabel("Total tracked time");

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
        add(projectSelector, constraints);

        constraints.gridy = 2;
        constraints.weightx = 0.2;
        add(totalTracked, constraints);

        constraints.weightx = 0.8;
        add(totalTrackedTimeField, constraints);

        model.previewObjects().subscribe(obj -> {
            if (obj instanceof Task) {
                setTask((Task) obj);
            }
        });
    }

    public void setTask(Task task) {
        previewedTaskId = task.getId();
        titleField.setText(task.getTitle());

        Project project = task.getProject();
        if (project == null)
            projectSelector.setSelectedIndex(0);
        else
            projectSelector.setSelectedItem(project.getName());

        long totalDurationMillis = taskRepository.findTotalTrackedTimeById(previewedTaskId);
        totalTrackedTimeField.setText(FormatUtils.millisToReadableDuration(totalDurationMillis));
    }

    @Override
    public void submit() {
        if (projectSelector.hasProjectSelected())
            if (projectSelector.hasCustomProject())
                projectRepository.insert(projectSelector.getSelectedProject());
        taskRepository.update(new Task(previewedTaskId, titleField.getText(), projectSelector.getSelectedProject()));
    }
}
