package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.ui.listmodel.TaskListModel;
import com.github.polydome.journow.ui.popup.TaskPopupMenu;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaskListView extends JPanel {
    @Inject
    public TaskListView(TaskListModel model, TaskPopupMenu taskPopupMenu) {
        JList<Task> list = new JList<>(model);
        list.setFixedCellHeight(40);
        list.setFixedCellWidth(280);
        list.addMouseListener(new TaskCellPopupListener(taskPopupMenu, model));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                System.out.println(list.getModel().getElementAt(list.getSelectedIndex()).getTitle());
            }
        });
        add(list);
        add(new JScrollPane(list));
    }

    private static class TaskCellPopupListener extends MouseAdapter {
        private final TaskPopupMenu popupMenu;
        private final TaskListModel model;

        private TaskCellPopupListener(TaskPopupMenu popupMenu, TaskListModel model) {
            this.popupMenu = popupMenu;
            this.model = model;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                JList<Task> list = (JList<Task>) e.getSource();
                int row = list.locationToIndex(e.getPoint());
                list.setSelectedIndex(row);
                popupMenu.show(e.getComponent(), e.getX(), e.getY(), model.getElementAt(row));
            }
        }
    }
}
