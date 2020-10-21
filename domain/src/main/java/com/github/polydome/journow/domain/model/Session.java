package com.github.polydome.journow.domain.model;

import java.time.Instant;

public class Session {
    private final Instant startedAt;
    private final Instant endedAt;
    private final Task task;

    public Session(Instant startedAt, Instant endedAt, Task task) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.task = task;
    }
}
