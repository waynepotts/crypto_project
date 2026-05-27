package com.wayne.restservices.dtos.coingecko;

import java.util.Map;

public record CoinGeckoExchangeResponseDto (
    Map<String, CoinGeckoExchangeRateDto> rates
) {
}