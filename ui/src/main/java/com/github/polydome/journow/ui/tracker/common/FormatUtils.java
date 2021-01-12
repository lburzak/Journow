package com.github.polydome.journow.ui.tracker.common;

import java.time.Duration;

public class FormatUtils {
    public static String formatDuration(Duration duration) {
        return String.format("%02d:%02d", duration.toMinutesPart(), duration.toSecondsPart());
    }
}
