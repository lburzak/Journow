package com.github.polydome.journow.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Session {
    private final long id;
    private final Instant startedAt;
    private final Instant endedAt;
    private final Task task;

    public Session(long id, Instant startedAt, Instant endedAt, Task task) {
        this.id = id;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.task = task;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public Task getTask() {
        return task;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return id == session.id &&
                startedAt.equals(session.startedAt) &&
                endedAt.equals(session.endedAt) &&
                Objects.equals(task, session.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startedAt, endedAt, task);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                ", task=" + task +
                '}';
    }
}
