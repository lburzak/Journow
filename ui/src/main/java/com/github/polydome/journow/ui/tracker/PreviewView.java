package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.ui.preview.EntityEditorForm;
import com.github.polydome.journow.ui.preview.TaskPreviewPane;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class PreviewView extends JPanel {
    @Inject
    public PreviewView(TaskPreviewPane taskPreviewPane) {

        setLayout(new BorderLayout());
        add(taskPreviewPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.8;
        bottomPanel.add(new JPanel(), c);
        c.weightx = 0.2;
        c.insets = new Insets(10, 10, 0, 10);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(a -> {
            ((EntityEditorForm) taskPreviewPane).submit();
        });
        bottomPanel.add(saveButton, c);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
}
