package com.wayne.restservices.jobs;

import com.wayne.restservices.services.CoinSyncService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoinSyncJob {

    private final CoinSyncService coinSyncService;

    public CoinSyncJob(
            CoinSyncService coinSyncService
    ) {
        this.coinSyncService = coinSyncService;
    }

    @Scheduled(fixedRate = 300000)
    public void syncCoins() {

        coinSyncService.syncCoins();
    }
}
