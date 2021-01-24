package com.github.polydome.journow.ui.listmodel;

import com.github.polydome.journow.data.event.DataEvent;
import com.github.polydome.journow.domain.model.Session;
import com.github.polydome.journow.domain.repository.SessionRepository;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;
import java.util.List;

public class SessionListModel extends AbstractListModel<Session> {
    private final List<Session> sessions;

    @Inject
    public SessionListModel(SessionRepository sessionRepository, @Named("SessionDataEvents") Observable<DataEvent> events) {
        sessions = sessionRepository.findAll();
        events.subscribe(ev -> {
            if (ev.getType() == DataEvent.Type.INSERT) {
                List<Session> freshTasks = sessionRepository.findAll();
                sessions.clear();
                sessions.addAll(freshTasks);
                var insertedTask = sessions.stream().filter(task -> task.getId() == ev.getIdStart()).findFirst();
                if (insertedTask.isPresent()) {
                    var index = sessions.indexOf(insertedTask.get());
                    fireIntervalAdded(this, index, index);
                } else {
                    System.err.println("Inserted session not found");
                }
            }
        });
    }

    @Override
    public int getSize() {
        return sessions.size();
    }

    @Override
    public Session getElementAt(int i) {
        return sessions.get(i);
    }

}
