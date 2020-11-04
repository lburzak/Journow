package com.github.polydome.journow.timedateparser;

import java.time.Instant;

public class TimeRange {
    private final Instant start;
    private final Instant end;

    public TimeRange(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
}
