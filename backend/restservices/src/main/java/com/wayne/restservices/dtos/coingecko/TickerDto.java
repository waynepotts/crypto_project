package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record TickerDto(

        String base,
        String target,

        MarketDto market,

        BigDecimal last,
        BigDecimal volume,

        @JsonProperty("converted_last")
        Map<String, BigDecimal> convertedLast,

        @JsonProperty("converted_volume")
        Map<String, BigDecimal> convertedVolume,

        @JsonProperty("bid_ask_spread_percentage")
        BigDecimal bidAskSpreadPercentage,

        Instant timestamp,

        @JsonProperty("last_traded_at")
        Instant lastTradedAt,

        @JsonProperty("last_fetch_at")
        Instant lastFetchAt,

        @JsonProperty("is_anomaly")
        Boolean isAnomaly,

        @JsonProperty("is_stale")
        Boolean isStale,

        @JsonProperty("trade_url")
        String tradeUrl,

        @JsonProperty("coin_id")
        String coinId,

        @JsonProperty("target_coin_id")
        String targetCoinId
) {
}
