package com.github.polydome.journow.ui.log;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LogTimeWindow extends JFrame {
    public LogTimeWindow() {
        setTitle("Journow - Log time");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center window on screen
        setLocationRelativeTo(null);

        JPanel mainPanel = new LogTimeIndex(new ExitAction());

        setSize(800, 64 + 20);
        setContentPane(mainPanel);

        setVisible(true);
    }

    private class ExitAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            dispose();
        }
    }
}
