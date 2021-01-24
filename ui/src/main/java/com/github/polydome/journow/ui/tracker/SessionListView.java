package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.ui.listmodel.SessionTableModel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class SessionListView extends JPanel {
    private static final int[] COLUMN_WIDTHS = {100, 100, 20, 300};

    @Inject
    public SessionListView(SessionTableModel model) {
        setLayout(new GridBagLayout());
        var constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        JTable table = new JTable(model);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableCellRenderer[] cellRenderers =
                new TableCellRenderer[]{centerRenderer, centerRenderer, centerRenderer, null};

        TableColumn column;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);

            if (cellRenderers[i] != null)
                column.setCellRenderer(cellRenderers[i]);
            column.setPreferredWidth(COLUMN_WIDTHS[i]);

        }

        add(table, constraints);
        add(new JScrollPane(table), constraints);

    }
}
