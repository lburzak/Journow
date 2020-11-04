package com.github.polydome.journow.timedateparser;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser {
    public Optional<TimeRange> parseRange() {
        return Optional.empty();
    }

    public Optional<Long> parseDuration(String input) {
        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*h", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            double hours = Float.parseFloat(matcher.group().substring(0, matcher.group().length() - 1));
            return Optional.of(TimeUnit.HOURS.toMillis((long) hours) + Math.round(TimeUnit.HOURS.toMillis(1) * (hours - ((long) hours))));
        }

        return Optional.empty();
    }
}
