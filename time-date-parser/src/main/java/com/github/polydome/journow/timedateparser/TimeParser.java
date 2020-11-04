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
            match = matcher.group();
            TimeUnit timeUnit;

            switch (Character.toLowerCase(match.charAt(match.length() - 1))) {
                case 'm':
                    timeUnit = TimeUnit.MINUTES;
                    break;
                case 'h':
                    timeUnit = TimeUnit.HOURS;
                    break;
                default:
                    throw new RuntimeException("Unimplemented identifier");
            }

            value = Float.parseFloat(match.substring(0, matcher.group().length() - 1));
            result += timeUnit.toMillis((long) value)
                    + Math.round(timeUnit.toMillis(1) * (value - ((long) value)));
        }

        if (result == 0)
            return Optional.empty();

        return Optional.of(result);
    }
}
