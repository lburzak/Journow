package com.github.polydome.journow.ui.popup;

import javax.swing.*;

public class TaskPopupMenu extends JPopupMenu {
    public TaskPopupMenu() {
        super("Task");
        JMenuItem startTrackingItem = new JMenuItem("Start tracking");
        add(startTrackingItem);
    }
}
