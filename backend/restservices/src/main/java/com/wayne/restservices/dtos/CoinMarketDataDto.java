package com.wayne.restservices.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record CoinMarketDataDto(
    Long id,
    Long coinId,
    String name,
    String symbol,
    BigDecimal currentPrice,
    BigDecimal marketCap,
    Integer marketCapRank,
    Instant lastUpdated,
    BigDecimal priceChange24h
) {}
