package com.github.polydome.journow.ui.tracker;

import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.ui.listmodel.SessionListModel;
import com.github.polydome.journow.ui.tracker.common.FormatUtils;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;

public class SessionListView extends JPanel {
    @Inject
    public SessionListView(SessionListModel model) {
        JList<Session> list = new JList<>(model);
        list.setFixedCellHeight(40);
        list.setFixedCellWidth(280);
        list.setCellRenderer(new SessionCellRenderer());
        add(list);
        add(new JScrollPane(list));
    }

    private static class SessionCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Session session = (Session) value;
            JPanel cell = new JPanel();

            cell.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.VERTICAL;
            constraints.insets = new Insets(5, 5, 5, 5);

            Duration duration = Duration.between(session.getStartedAt(), session.getEndedAt());
            JLabel durationText = new JLabel(FormatUtils.formatDuration(duration));
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.PAGE_START;
            cell.add(durationText, constraints);

            JLabel titleText = new JLabel(session.getTask().getTitle());
            titleText.setHorizontalAlignment(LEFT);
            constraints.gridx = 1;
            constraints.weightx = 1;
            constraints.anchor = GridBagConstraints.PAGE_START;
            constraints.fill = GridBagConstraints.BOTH;
            cell.add(titleText, constraints);

            return cell;
        }
    }
}
