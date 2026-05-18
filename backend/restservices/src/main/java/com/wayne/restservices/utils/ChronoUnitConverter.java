package com.wayne.restservices.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class ChronoUnitConverter
        implements AttributeConverter<ChronoUnit, Short> {

    @Override
    public Short convertToDatabaseColumn(
            ChronoUnit attribute
    ) {

        return attribute == null
                ? null
                : TO_DB.get(attribute);
    }

    @Override
    public ChronoUnit convertToEntityAttribute(
            Short dbData
    ) {

        return dbData == null
                ? null
                : FROM_DB.get(dbData);
    }

    public static final Map<ChronoUnit, Short> TO_DB =
            Map.of(
                    ChronoUnit.MINUTES, (short) 1,
                    ChronoUnit.HOURS, (short) 2,
                    ChronoUnit.DAYS, (short) 3
            );

    public static final Map<Short, ChronoUnit> FROM_DB =
            TO_DB.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getValue,
                            Map.Entry::getKey
                    ));
    public static Instant normalizeFiveMinutes(
            Instant instant
    ) {

        long epochSeconds = instant.getEpochSecond();

        long fiveMinuteBucket =
                epochSeconds - (epochSeconds % 300);

        return Instant.ofEpochSecond(
                fiveMinuteBucket
        );
    }
    public static Instant normalizeHourly(
            Instant instant
    ) {

        return instant
                .truncatedTo(ChronoUnit.HOURS);
    }
    public static Instant normalizeDaily(
            Instant instant
    ) {

        return instant
                .truncatedTo(ChronoUnit.DAYS);
    }
}
