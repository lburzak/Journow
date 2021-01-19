package com.github.polydome.journow.ui.dialog;

import javax.swing.*;

public class LogDialog extends JDialog {
    public LogDialog() {
        setTitle("Journow - Log session");

        setLocationRelativeTo(null);

        setAlwaysOnTop(true);
        setModal(true);
        setSize(100, 100);
        setVisible(true);
    }
}
