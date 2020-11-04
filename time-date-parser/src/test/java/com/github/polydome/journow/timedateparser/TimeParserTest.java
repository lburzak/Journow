package com.github.polydome.journow.timedateparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TimeParserTest {
    TimeParser SUT = new TimeParser();

    final int MINUTES_IN_HOUR = 60;
    final int SECONDS_IN_MINUTE = 60;
    final int MILLISECONDS_IN_SECOND = 1000;
    final int MILLISECONDS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;
    final int MILLISECONDS_IN_MINUTE =  SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;

    @Test
    public void parseDuration_emptyString_returnsEmpty() {
        Optional<Long> duration = SUT.parseDuration("");

        assertThat(duration.isEmpty(), equalTo(true));
    }

    @Test
    public void parseDuration_2h_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("2h");

        assertThat(duration.isPresent(), equalTo(true));
        assertThat(duration.get(), equalTo((2L * MILLISECONDS_IN_HOUR)));
    }

    @Test
    public void parseDuration_37h_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("37h");

        assertThat(duration.isPresent(), equalTo(true));
        assertThat(duration.get(), equalTo((37L * MILLISECONDS_IN_HOUR)));
    }

    @Test
    public void parseDuration_37dot3h_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("37.3h");

        assertThat(duration.isPresent(), equalTo(true));

        assertWithinAcceptableDifference((long) (37.3 * MILLISECONDS_IN_HOUR), duration.get());
    }

    @Test
    public void parseDuration_12m_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("12m");

        assertThat(duration.isPresent(), equalTo(true));

        assertThat(duration.get(), equalTo((12L * MILLISECONDS_IN_MINUTE)));
    }

    @Test
    public void parseDuration_85m_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("85m");

        assertThat(duration.isPresent(), equalTo(true));

        assertThat(duration.get(), equalTo((85L * MILLISECONDS_IN_MINUTE)));
    }

    @Test
    public void parseDuration_85_16m_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("85.16m");

        assertThat(duration.isPresent(), equalTo(true));

        assertWithinAcceptableDifference((long) (85.16 * MILLISECONDS_IN_MINUTE), duration.get());
    }

    @Test
    public void parseDuration_85_16m_15_875h_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("85.16m 15.875h");

        assertThat(duration.isPresent(), equalTo(true));

        assertWithinAcceptableDifference(((long) (85.16 * MILLISECONDS_IN_MINUTE)) + ((long) (15.875 * MILLISECONDS_IN_HOUR)), duration.get());
    }

    @Test
    public void parseDuration_multipleStatements_returnsMilliseconds() {
        Optional<Long> duration = SUT.parseDuration("85.16m 15.875h 12m");

        assertThat(duration.isPresent(), equalTo(true));

        assertWithinAcceptableDifference(
                ((long) (85.16 * MILLISECONDS_IN_MINUTE))
                + ((long) (15.875 * MILLISECONDS_IN_HOUR))
                + ((long) (12 * MILLISECONDS_IN_MINUTE))
        , duration.get());
    }

    private void assertWithinAcceptableDifference(long a, long b) {
        assertThat(Math.abs(a - b), lessThan(100L));
    }

}