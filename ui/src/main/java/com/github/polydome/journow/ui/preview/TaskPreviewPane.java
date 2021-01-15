package com.github.polydome.journow.ui.preview;

import com.github.polydome.journow.domain.model.Task;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class TaskPreviewPane extends JPanel {
    private final JTextField titleField = new JTextField("Hello there");

    @Inject
    public TaskPreviewPane(PreviewModel model) {
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
        titleField.setText(task.getTitle());
    }
}
