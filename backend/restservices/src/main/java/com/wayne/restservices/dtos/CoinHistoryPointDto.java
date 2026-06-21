package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record CoinHistoryPointDto(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Instant timestamp,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal price,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal marketCap,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal volume
) implements Serializable {}
