package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record CoinResponseDto(
    @Schema(description = "Internal database ID", example = "1") Long id,
    @Schema(description = "CoinGecko ID", example = "bitcoin") String coingeckoId,
    @Schema(description = "Coin symbol", example = "BTC") String symbol,
    @Schema(description = "Display name", example = "Bitcoin") String name,
    @Schema(description = "URL to the logo image", example = "www.url.com/btc.jpg") String image
) {}
