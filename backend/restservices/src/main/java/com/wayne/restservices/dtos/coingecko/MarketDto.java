package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MarketDto(
        String name,
        String identifier,

        @JsonProperty("has_trading_incentive")
        Boolean hasTradingIncentive
) {
}
