package com.wayne.restservices.utils;

import org.junit.jupiter.api.Test;

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
}
