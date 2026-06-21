package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record CoinMarketDataDto(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long coinId,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String symbol,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal currentPrice,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal marketCap,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer marketCapRank,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Instant lastUpdated,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal priceChange24h
) implements Serializable {}
