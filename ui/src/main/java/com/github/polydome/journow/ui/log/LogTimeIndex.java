package com.github.polydome.journow.ui.log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class LogTimeIndex extends JPanel {
    private final SpringLayout layout = new SpringLayout();
    private final JButton logButton = createLogButton();
    private final JTextField inputField = createInputField();

    public LogTimeIndex(AbstractAction exitAction) {
        super();
        int margin = 10;
        setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        setLayout(layout);
        add(inputField);
        add(logButton);

        logButton.addActionListener(actionEvent -> System.out.println("perf"));
        setupKeys(exitAction);

        setVisible(true);
    }

    private JButton createLogButton() {
        JButton button = new JButton("Log");
        button.setSize(0, 30);layout.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.SOUTH, button, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.EAST, button, -5, SpringLayout.EAST, this);

        return button;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField(10);
        field.setSize(500, 40);

        layout.putConstraint(SpringLayout.NORTH, field, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, field, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.WEST, field, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, field, -5, SpringLayout.WEST, logButton);

        return field;
    }

    private void setupKeys(AbstractAction exitAction) {
        inputField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "close");
        inputField.getActionMap().put("close", exitAction);

        inputField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "log");
        inputField.getActionMap().put("log", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logButton.doClick();
            }
        });
    }
}
