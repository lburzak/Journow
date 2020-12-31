package com.github.polydome.journow.ui.tracker;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class SessionListView extends JPanel {
    @Inject
    public SessionListView() {
        add(new TextField("This is session list"));
    }
}
