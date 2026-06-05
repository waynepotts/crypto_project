package com.wayne.restservices.jobs.listeners;

import com.wayne.restservices.jobs.SyncTracker;
import com.wayne.restservices.jobs.events.CoinMarketDataSyncRequestEvent;
import com.wayne.restservices.services.CoinMarketDataSyncService;
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

    @Async
    @EventListener
    public void handle(CoinMarketDataSyncRequestEvent event) {
        String key = event.coinId() + "-" + event.from() + "-" + event.to();
        if (!syncTracker.start(key)) {
            return;
        }
        try {
            coinMarketDataSyncService.syncMissingRange(event.coinId(), event.from(), event.to());
        } finally {
            syncTracker.finish(key);
        }
    }
}
