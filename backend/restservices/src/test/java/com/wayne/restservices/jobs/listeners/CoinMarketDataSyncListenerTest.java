package com.wayne.restservices.jobs.listeners;

import com.wayne.restservices.jobs.SyncTracker;
import com.wayne.restservices.jobs.events.CoinMarketDataSyncRequestEvent;
import com.wayne.restservices.services.CoinMarketDataSyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class CoinMarketDataSyncListenerTest {

    @Mock
    private CoinMarketDataSyncService coinMarketDataSyncService;

    @Spy
    private SyncTracker syncTracker = new SyncTracker();

    // A synchronous executor to make @Async calls execute inline during tests
    private final Executor synchronousExecutor = Runnable::run;

    @InjectMocks
    private CoinMarketDataSyncListener listener;

    @Captor
    private ArgumentCaptor<Long> coinIdCaptor;

    @Captor
    private ArgumentCaptor<Instant> fromCaptor;

    @Captor
    private ArgumentCaptor<Instant> toCaptor;

    @Test
    void shouldSyncWhenNoDuplicateExists() {
        Long coinId = 1L;
        Instant from = Instant.parse("2024-01-01T00:00:00Z");
        Instant to = Instant.parse("2024-01-02T00:00:00Z");

        listener.handle(new CoinMarketDataSyncRequestEvent(coinId, from, to));

        verify(coinMarketDataSyncService).syncMissingRange(coinIdCaptor.capture(), fromCaptor.capture(), toCaptor.capture());
        assertThat(coinIdCaptor.allValues).containsExactly(coinId);
        assertThat(fromCaptor.allValues).containsExactly(from);
        assertThat(toCaptor.allValues).containsExactly(to);
    }

    @Test
    void shouldNotSyncWhenDuplicateExists() {
        Long coinId = 1L;
        Instant from = Instant.parse("2024-01-01T00:00:00Z");
        Instant to = Instant.parse("2024-01-02T00:00:00Z");

        String key = coinId + "-" + from + "-" + to;
        syncTracker.start(key);

        listener.handle(new CoinMarketDataSyncRequestEvent(coinId, from, to));

        verify(coinMarketDataSyncService, never()).syncMissingRange(anyLong(), any(), any());
    }

    @Test
    void shouldCleanUpAfterSuccessfulSync() {
        Long coinId = 1L;
        Instant from = Instant.parse("2024-01-01T00:00:00Z");
        Instant to = Instant.parse("2024-01-02T00:00:00Z");

        listener.handle(new CoinMarketDataSyncRequestEvent(coinId, from, to));

        String key = coinId + "-" + from + "-" + to;
        assertThat(syncTracker.start(key)).isTrue();
    }

    @Test
    void shouldCleanUpAfterFailedSync() {
        Long coinId = 1L;
        Instant from = Instant.parse("2024-01-01T00:00:00Z");
        Instant to = Instant.parse("2024-01-02T00:00:00Z");

        doThrow(new RuntimeException("sync error")).when(coinMarketDataSyncService)
                .syncMissingRange(coinId, from, to);

        listener.handle(new CoinMarketDataSyncRequestEvent(coinId, from, to));

        String key = coinId + "-" + from + "-" + to;
        assertThat(syncTracker.start(key)).isTrue();

        verify(coinMarketDataSyncService).syncMissingRange(coinId, from, to);
    }
}
