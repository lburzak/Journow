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
        Pattern pattern = Pattern.compile("(?<hours>[0-9]*\\.?[0-9]*h)|(?<minutes>[0-9]*\\.?[0-9]*m)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        long result = 0;
        String match;
        double value;

        while (matcher.find()) {
            match = matcher.group("hours");
            if (match != null) {
                value = Float.parseFloat(match.substring(0, matcher.group().length() - 1));
                result += TimeUnit.HOURS.toMillis((long) value)
                        + Math.round(TimeUnit.HOURS.toMillis(1) * (value - ((long) value)));
            }

            match = matcher.group("minutes");
            if (match != null) {
                value = Float.parseFloat(match.substring(0, matcher.group().length() - 1));
                result += TimeUnit.MINUTES.toMillis((long) value)
                        + Math.round(TimeUnit.MINUTES.toMillis(1) * (value - ((long) value)));
            }
        }

        if (result == 0)
            return Optional.empty();

        return Optional.of(result);
    }
}
