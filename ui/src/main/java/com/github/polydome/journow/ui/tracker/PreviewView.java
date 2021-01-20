package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.ui.preview.EntityEditorForm;
import com.github.polydome.journow.ui.preview.PreviewModel;
import com.github.polydome.journow.ui.preview.ProjectPreviewPane;
import com.github.polydome.journow.ui.preview.TaskPreviewPane;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class PreviewView extends JPanel {
    private EntityEditorForm currentForm;

    @Inject
    public PreviewView(TaskPreviewPane taskPreviewPane, ProjectPreviewPane projectPreviewPane, PreviewModel previewModel) {
        setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.8;
        bottomPanel.add(new JPanel(), c);

        c.weightx = 0.2;
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(a -> {
            currentForm.submit();
        });
        bottomPanel.add(saveButton, c);
        add(bottomPanel, BorderLayout.PAGE_END);

        previewModel.previewObjects().subscribe(obj -> {
            if (obj instanceof Task) {
                projectPreviewPane.setVisible(false);
                remove(projectPreviewPane);
                add(taskPreviewPane, BorderLayout.CENTER);
                taskPreviewPane.setVisible(true);
                currentForm = taskPreviewPane;
            } else if (obj instanceof Project) {
                taskPreviewPane.setVisible(false);
                remove(taskPreviewPane);
                add(projectPreviewPane, BorderLayout.CENTER);
                projectPreviewPane.setVisible(true);
                currentForm = projectPreviewPane;
            }
        });
    }
}
