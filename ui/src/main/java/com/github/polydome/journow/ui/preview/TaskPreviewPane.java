package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Task;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskPreviewPane extends JPanel {
    private final JTextField titleField = new JTextField();

    @Inject
    public TaskPreviewPane(PreviewModel model) {
        JLabel titleLabel = new JLabel("Title");
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        add(titleLabel, constraints);
        add(titleField, constraints);
        model.previewObjects().subscribe(obj -> {
            if (obj instanceof Task)
                setTask((Task) obj);
            else {
                throw new UnsupportedOperationException("Unsupported preview object");
            }
        });
    }

    public void setTask(Task task) {
        titleField.setText(task.getTitle());
    }
}
