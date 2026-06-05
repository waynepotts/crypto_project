package com.wayne.restservices.jobs.listeners;

import com.wayne.restservices.jobs.SyncTracker;
import com.wayne.restservices.jobs.events.CoinMarketDataSyncRequestEvent;
import com.wayne.restservices.services.CoinMarketDataSyncService;

import java.time.Instant;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CoinMarketDataSyncListener {

    private final CoinMarketDataSyncService coinMarketDataSyncService;
    private final SyncTracker syncTracker;

    public CoinMarketDataSyncListener(CoinMarketDataSyncService coinMarketDataSyncService, SyncTracker syncTracker) {
        this.coinMarketDataSyncService = coinMarketDataSyncService;
        this.syncTracker = syncTracker;
    }

    @EventListener
    public void handle(CoinMarketDataSyncRequestEvent event) {
        Long coinId = event.coinId();
        Instant from = event.from();
        Instant to = event.to();
        String key = coinId + "-" + from + "-" + to;

        if (!syncTracker.start(key)) {
            return;
        }
        try {
            coinMarketDataSyncService.syncMissingRange(coinId, from, to);
        } finally {
            syncTracker.finish(key);
        }
    }
}
