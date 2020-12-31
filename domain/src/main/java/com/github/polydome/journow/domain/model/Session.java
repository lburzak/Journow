package com.github.polydome.journow.domain.model;

import java.time.Instant;

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
}
