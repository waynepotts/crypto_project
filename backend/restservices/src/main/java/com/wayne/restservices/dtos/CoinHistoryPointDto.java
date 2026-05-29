package com.wayne.restservices.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record CoinHistoryPointDto(
    Instant timestamp,
    BigDecimal price,
    BigDecimal marketCap,
    BigDecimal volume
) {}
