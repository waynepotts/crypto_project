package com.wayne.restservices.jobs;

import com.wayne.restservices.services.CoinMarketDataService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoinSyncJob {

    private final CoinMarketDataService coinMarketDataService;

    public CoinSyncJob(
            CoinMarketDataService coinMarketDataService) {
        this.coinMarketDataService = coinMarketDataService;
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 300000)
    public void syncCoins() {

        coinMarketDataService.syncCoins();
    }
}
