package com.wayne.restservices.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ChronoUnitConverterTest {

    private final ChronoUnitConverter converter = new ChronoUnitConverter();

    @Test
    void shouldConvertMinutesToShort() {
        assertEquals(Short.valueOf((short) 1), converter.convertToDatabaseColumn(ChronoUnit.MINUTES));
    }

    @Test
    void shouldConvertHoursToShort() {
        assertEquals(Short.valueOf((short) 2), converter.convertToDatabaseColumn(ChronoUnit.HOURS));
    }

    @Test
    void shouldConvertDaysToShort() {
        assertEquals(Short.valueOf((short) 3), converter.convertToDatabaseColumn(ChronoUnit.DAYS));
    }

    @Test
    void shouldReturnNullWhenConvertingNullChronoUnit() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void shouldConvertShortToMinutes() {
        assertEquals(ChronoUnit.MINUTES, converter.convertToEntityAttribute((short) 1));
    }

    @Test
    void shouldConvertShortToHours() {
        assertEquals(ChronoUnit.HOURS, converter.convertToEntityAttribute((short) 2));
    }

    @Test
    void shouldConvertShortToDays() {
        assertEquals(ChronoUnit.DAYS, converter.convertToEntityAttribute((short) 3));
    }

    @Test
    void shouldReturnNullWhenConvertingNullShort() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldNormalizeToFiveMinutes() {
        Instant input = Instant.parse("2026-05-25T13:47:32Z");
        Instant expected = Instant.parse("2026-05-25T13:45:00Z");
        assertEquals(expected, ChronoUnitConverter.normalizeFiveMinutes(input));
    }

    @Test
    void shouldNormalizeToExactFiveMinuteBoundary() {
        Instant input = Instant.parse("2026-05-25T13:45:00Z");
        assertEquals(input, ChronoUnitConverter.normalizeFiveMinutes(input));
    }

    @Test
    void shouldNormalizeHourly() {
        Instant input = Instant.parse("2026-05-25T13:47:32Z");
        Instant expected = Instant.parse("2026-05-25T13:00:00Z");
        assertEquals(expected, ChronoUnitConverter.normalizeHourly(input));
    }

    @Test
    void shouldNormalizeDaily() {
        Instant input = Instant.parse("2026-05-25T13:47:32Z");
        Instant expected = Instant.parse("2026-05-25T00:00:00Z");
        assertEquals(expected, ChronoUnitConverter.normalizeDaily(input));
    }

    @Test
    void shouldGetGranularityMinutesWhenNotOnBoundaries() {
        Instant input = Instant.parse("2026-05-25T13:47:32Z");
        ChronoUnit result = ChronoUnitConverter.getGranularity(input);
        assertEquals(ChronoUnit.MINUTES, result);
    }

    @Test
    void shouldGetGranularityHoursWhenOnHourBoundary() {
        Instant input = Instant.parse("2026-05-25T14:00:00Z");
        ChronoUnit result = ChronoUnitConverter.getGranularity(input);
        assertEquals(ChronoUnit.HOURS, result);
    }

    @Test
    void shouldGetGranularityHoursNearHourBoundary() {
        Instant input = Instant.parse("2026-05-25T14:01:00Z");
        ChronoUnit result = ChronoUnitConverter.getGranularity(input);
        assertEquals(ChronoUnit.HOURS, result);
    }

    @Test
    void shouldGetGranularityDaysWhenOnMidnightBoundary() {
        Instant input = Instant.parse("2026-05-25T00:00:00Z");
        ChronoUnit result = ChronoUnitConverter.getGranularity(input);
        assertEquals(ChronoUnit.DAYS, result);
    }

    @Test
    void shouldGetGranularityDaysNearMidnight() {
        Instant input = Instant.parse("2026-05-24T00:04:59Z");
        ChronoUnit result = ChronoUnitConverter.getGranularity(input);
        assertEquals(ChronoUnit.DAYS, result);
    }

    @Test
    void shouldGetGranularityHoursInMiddleOfHour() {
        Instant input = Instant.parse("2026-05-25T14:30:00Z");
        ChronoUnit result = ChronoUnitConverter.getGranularity(input);
        assertEquals(ChronoUnit.MINUTES, result);
    }

    @Test
    void shouldGetHoursGranularityFromBoundariesWithDaysBetweenBoundaries() {
        Duration duration = Duration.ofDays(10);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration, 7, 30);
        assertEquals(ChronoUnit.HOURS, result);
    }

    @Test
    void shouldGetDaysGranularityFromBoundariesWithLargeDuration() {
        Duration duration = Duration.ofDays(45);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration, 7, 30);
        assertEquals(ChronoUnit.DAYS, result);
    }

    @Test
    void shouldGetMinutesGranularityFromBoundariesWithSmallDuration() {
        Duration duration = Duration.ofDays(5);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration, 7, 30);
        assertEquals(ChronoUnit.MINUTES, result);
    }

    @Test
    void shouldGetMinutesGranularityFromBoundariesWithSmallDurationAndDefaultBoundary() {
        Duration duration = Duration.ofDays(5);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration);
        assertEquals(ChronoUnit.MINUTES, result);
    }

    @Test
    void shouldGetHoursGranularityFromBoundariesWithDefaultBoundary() {
        Duration duration = Duration.ofDays(10);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration);
        assertEquals(ChronoUnit.HOURS, result);
    }

    @Test
    void shouldGetDaysGranularityFromBoundariesWithLargeDurationAndDefaultBoundary() {
        Duration duration = Duration.ofDays(35);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration);
        assertEquals(ChronoUnit.DAYS, result);
    }

    @Test
    void shouldGetHoursGranularityFromBoundariesAtHourThreshold() {
        // TODO: change these boundary tests to be days plus or minus one second
        Duration duration = Duration.ofDays(8);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration, 7, 30);
        assertEquals(ChronoUnit.HOURS, result);
    }

    @Test
    void shouldGetMinutesGranularityFromBoundariesJustBelowThreshold() {
        Duration duration = Duration.ofDays(8);
        duration = duration.minusMillis(1);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration, 7, 30);
        assertEquals(ChronoUnit.MINUTES, result);
    }

    @Test
    void shouldGetMinutesGranularityFromBoundariesWithNegativeDuration() {
        Duration duration = Duration.ofHours(-1);
        ChronoUnit result = ChronoUnitConverter.FromBoundaries(duration);
        assertEquals(ChronoUnit.MINUTES, result);
    }
}
