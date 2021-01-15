package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.ui.preview.TaskPreviewPane;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class PreviewView extends JPanel {
    @Inject
    public PreviewView(TaskPreviewPane taskPreviewPane) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.weightx = 1;
        add(taskPreviewPane, constraints);
    }
}
