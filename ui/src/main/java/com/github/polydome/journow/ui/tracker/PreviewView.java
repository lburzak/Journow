package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.ui.preview.TaskPreviewPane;

import javax.inject.Inject;
import javax.swing.*;

public class PreviewView extends JPanel {
    @Inject
    public PreviewView(TaskPreviewPane taskPreviewPane) {
        add(taskPreviewPane);
    }
}
