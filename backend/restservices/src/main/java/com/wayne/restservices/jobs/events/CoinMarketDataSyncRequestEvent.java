package com.wayne.restservices.jobs.events;

import java.time.Instant;

public record CoinMarketDataSyncRequestEvent(
        Long coinId,
        Instant from,
        Instant to) {
}
