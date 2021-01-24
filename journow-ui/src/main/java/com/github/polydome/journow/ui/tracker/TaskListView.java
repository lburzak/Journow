package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.ui.listmodel.TaskTreeModel;
import com.github.polydome.journow.ui.popup.TaskPopupMenu;
import com.github.polydome.journow.ui.preview.PreviewModel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaskListView extends JPanel {
    @Inject
    public TaskListView(TaskTreeModel model, TaskPopupMenu taskPopupMenu, PreviewModel previewModel) {
        JTree tree = new TaskTree(model);
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(new TaskCellPopupListener(taskPopupMenu, model));
        tree.addTreeSelectionListener(ev -> {
            TreePath path = ev.getNewLeadSelectionPath();
            if (path != null) {
                Object item = path.getLastPathComponent();
                if (item instanceof DefaultMutableTreeNode) {
                    Object obj = ((DefaultMutableTreeNode) item).getUserObject();
                    if (obj instanceof Task)
                        previewModel.previewTask((Task) ((DefaultMutableTreeNode) item).getUserObject());
                    else if (obj instanceof Project)
                        previewModel.previewProject(((Project) obj));
                }
            }
        });

        model.reloads().subscribe(e -> {
            for (int i = 0; i < tree.getRowCount(); i++) {
                tree.expandRow(i);
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
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    Object item = path.getLastPathComponent();
                    if (item instanceof DefaultMutableTreeNode) {
                        if (((DefaultMutableTreeNode) item).getUserObject() instanceof Task)
                            popupMenu.show(e.getComponent(), e.getX(), e.getY(), (Task) ((DefaultMutableTreeNode) item).getUserObject());

                        tree.setSelectionPath(path);
                    }
                }
            }
        }
    }

    private static class TaskTree extends JTree {
        public TaskTree(TreeModel newModel) {
            super(newModel);
        }

        @Override
        public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof DefaultMutableTreeNode)
                value = formatObject(((DefaultMutableTreeNode) value).getUserObject());

            return formatObject(value);
        }

        private String formatObject(Object object) {
            if (object instanceof Task)
                return formatTask((Task) object);
            else if (object instanceof Project)
                return formatProject((Project) object);
            else if (object == null)
                return "";
            else
                return object.toString();
        }

        private String formatProject(Project project) {
            return "<html><b><font color=#AAAAAA>#" + project.getId() +" <font color=#000000>" + project.getName() + "</html>";
        }

        private String formatTask(Task task) {
            return "<html><font color=#AAAAAA>#" + task.getId() +" <font color=#000000>" + task.getTitle() + "</html>";
        }
    }
}
