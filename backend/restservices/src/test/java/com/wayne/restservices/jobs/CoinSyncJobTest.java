package com.wayne.restservices.jobs;

import com.wayne.restservices.services.CoinMarketDataService;
import com.wayne.restservices.services.CoinService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinSyncJobTest {

    @Mock
    private CoinMarketDataService coinMarketDataService;

    @Mock
    private CoinService coinService;

    @InjectMocks
    private CoinSyncJob coinSyncJob;

    @Test
    void shouldSyncCoins() {
        coinSyncJob.syncCoins();

        verify(coinMarketDataService, times(1)).syncCoins();
    }

    @Test
    void shouldNotInvokeCoinServiceDuringSync() {
        coinSyncJob.syncCoins();

        verify(coinMarketDataService, times(1)).syncCoins();
        verifyNoInteractions(coinService);
    }
}
