package com.wayne.restservices.jobs;

import com.wayne.restservices.services.CoinMarketDataService;

import com.wayne.restservices.services.CoinService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoinSyncJob {

    private final CoinMarketDataService coinMarketDataService;
    private final CoinService coinService;

    public CoinSyncJob(
            CoinMarketDataService coinMarketDataService, CoinService coinService) {
        this.coinMarketDataService = coinMarketDataService;
        this.coinService = coinService;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void syncCoins() {
        coinMarketDataService.syncCoins();
    }

    @Scheduled(cron = "* * * 5 * ?")
    public void syncCategories(){
        coinService.updateCoinCategories();
    }
}
