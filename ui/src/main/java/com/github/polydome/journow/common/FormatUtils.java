package com.github.polydome.journow.common;

import java.time.Duration;

public class FormatUtils {
    public static String millisToReadableDuration(long millis) {
        Duration duration = Duration.ofMillis(millis);
        return FormatUtils.formatDuration(duration);
    }

    public static String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }
}
