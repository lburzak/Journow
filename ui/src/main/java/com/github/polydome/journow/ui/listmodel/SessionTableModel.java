package com.github.polydome.journow.ui.listmodel;

import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.common.FormatUtils;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.table.AbstractTableModel;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SessionTableModel extends AbstractTableModel {
    private final List<Session> sessions;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy hh:mm")
            .withLocale( Locale.getDefault() )
            .withZone( ZoneId.systemDefault() );

    @Inject
    public SessionTableModel(SessionRepository sessionRepository, @Named("SessionDataEvents") Observable<DataEvent> events) {
        sessions = sessionRepository.findAll();
        events.subscribe(ev -> {
            if (ev.getType() == DataEvent.Type.INSERT) {
                List<Session> freshTasks = sessionRepository.findAll();
                sessions.clear();
                sessions.addAll(freshTasks);
                var insertedTask = sessions.stream().filter(task -> task.getId() == ev.getIdStart()).findFirst();
                if (insertedTask.isPresent()) {
                    var index = sessions.indexOf(insertedTask.get());
                    fireTableRowsInserted(index, index);
                } else {
                    System.err.println("Inserted session not found");
                }
            }
        });
    }

    @Override
    public int getRowCount() {
        return sessions.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Session session = sessions.get(row);
        switch (col) {
            case 0:
                return dateTimeFormatter.format(session.getStartedAt());
            case 1:
                return dateTimeFormatter.format(session.getEndedAt());
            case 2:
                return FormatUtils.formatDuration(Duration.between(session.getStartedAt(), session.getEndedAt()));
            case 3:
                return session.getTask().getTitle();
        }

        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Date started";
            case 1:
                return "Date ended";
            case 2:
                return "Duration";
            case 3:
                return "Task";
        }

        return "";
    }
}
