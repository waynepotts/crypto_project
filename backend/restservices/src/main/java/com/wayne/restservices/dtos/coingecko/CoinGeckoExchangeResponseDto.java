package com.wayne.restservices.dtos.coingecko;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record CoinGeckoExchangeResponseDto (
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Map<String, CoinGeckoExchangeRateDto> rates
) {
}