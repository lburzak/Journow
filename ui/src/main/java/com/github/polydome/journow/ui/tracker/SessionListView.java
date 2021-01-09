package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.ui.listmodel.SessionListModel;

import javax.inject.Inject;
import javax.swing.*;

public class SessionListView extends JPanel {
    @Inject
    public SessionListView(SessionListModel model) {
        JList<Session> list = new JList<>(model);
        list.setFixedCellHeight(40);
        list.setFixedCellWidth(280);
        add(list);
    }
}
