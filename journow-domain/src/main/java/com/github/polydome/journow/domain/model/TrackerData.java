package com.github.polydome.journow.domain.model;

import java.time.Instant;

public class TrackerData {
    private final long taskId;
    private final Instant startTime;

    public TrackerData(long taskId, Instant startTime) {
        this.taskId = taskId;
        this.startTime = startTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public Instant getStartTime() {
        return startTime;
    }
}
