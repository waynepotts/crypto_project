package com.wayne.restservices.dtos.coingecko;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


public record CoinGeckoExchangeRateDto(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String unit,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal value,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        String type) {

}
