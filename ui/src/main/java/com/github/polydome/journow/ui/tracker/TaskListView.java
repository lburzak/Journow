package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.ui.listmodel.TaskListModel;
import com.github.polydome.journow.ui.listmodel.TaskTreeModel;
import com.github.polydome.journow.ui.popup.TaskPopupMenu;
import com.github.polydome.journow.ui.preview.PreviewModel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaskListView extends JPanel {
    @Inject
    public TaskListView(TaskTreeModel model, TaskPopupMenu taskPopupMenu, PreviewModel previewModel) {
        JTree tree = new JTree(model);
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(new TaskCellPopupListener(taskPopupMenu, model));
        tree.addTreeSelectionListener(ev -> {
            TreePath path = ev.getNewLeadSelectionPath();
            if (path != null) {
                Object item = model.getObjectByLabel(path.getLastPathComponent().toString());
                if (item instanceof Task) {
                    previewModel.previewTask((Task) item);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    private static class TaskCellPopupListener extends MouseAdapter {
        private final TaskPopupMenu popupMenu;
        private final TaskTreeModel model;

        private TaskCellPopupListener(TaskPopupMenu popupMenu, TaskTreeModel model) {
            this.popupMenu = popupMenu;
            this.model = model;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                JTree tree = (JTree) e.getSource();
                TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
                if (tp != null) {
                    Object selectedObj = model.getObjectByLabel(tp.getLastPathComponent().toString());
                    if (selectedObj instanceof Task)
                        popupMenu.show(e.getComponent(), e.getX(), e.getY(), (Task) selectedObj);
                    tree.setSelectionPath(tp);
                }
            }
        }
    }
}
