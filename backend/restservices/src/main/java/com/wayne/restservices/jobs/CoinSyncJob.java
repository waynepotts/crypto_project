package com.wayne.restservices.jobs;

import com.wayne.restservices.services.CoinMarketDataService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoinSyncJob {

    private final CoinMarketDataService coinMarketDataService;

    public CoinSyncJob(
            CoinMarketDataService coinMarketDataService) {
        this.coinMarketDataService = coinMarketDataService;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void syncCoins() {
        coinMarketDataService.syncCoins();
    }
}
