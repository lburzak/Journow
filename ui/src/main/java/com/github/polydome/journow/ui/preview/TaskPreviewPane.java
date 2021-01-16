package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.TaskRepository;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskPreviewPane extends JPanel implements EntityEditorForm {
    private final JTextField titleField = new JTextField("Hello there");
    private final TaskRepository taskRepository;

    private long previewedTaskId = -1;

    @Inject
    public TaskPreviewPane(PreviewModel model, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        JLabel titleLabel = new JLabel("Title");
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.2;
        constraints.anchor = GridBagConstraints.CENTER;

        add(titleLabel, constraints);
        constraints.weightx = 0.8;
        add(titleField, constraints);

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
    }

    @Override
    public void submit() {
        taskRepository.update(new Task(previewedTaskId, titleField.getText(), project));
    }
}
