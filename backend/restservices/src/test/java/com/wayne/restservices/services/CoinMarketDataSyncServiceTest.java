package com.wayne.restservices.services;


import com.wayne.restservices.clients.CoinGeckoClient;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoChartPointDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoMarketChartDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.mappers.CoinMarketDataMapper;
import com.wayne.restservices.repositories.CoinMarketDataRepository;
import com.wayne.restservices.repositories.CoinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinMarketDataSyncServiceTest {

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @Mock
    private CoinMarketDataRepository repository;

    @Mock
    private CoinRepository coinRepository;

    @InjectMocks
    private CoinMarketDataSyncService syncService;

    private Coin testCoin;


    @BeforeEach
    void setUp() {
        testCoin = new Coin();
        testCoin.setId(1L);
        testCoin.setCoingeckoId("bitcoin");
        testCoin.setSymbol("BTC");
        testCoin.setName("Bitcoin");
    }

    @Test
    void shouldThrowExceptionWhenCoinNotFound() {
        Instant from = Instant.parse("2026-01-01T00:00:00Z");
        Instant to = Instant.now();

        when(coinRepository.findById(999L)).thenReturn(Optional.empty());

        CoinNotFoundException exception = assertThrows(CoinNotFoundException.class, () -> {
            syncService.syncMissingRange(999L, from, to);
        });
        assertTrue(exception.getMessage().endsWith("999"));
        verify(coinRepository).findById(999L);
        verifyNoMoreInteractions(coinGeckoClient);
    }

    @Test
    void shouldHandleExceptionGracefully() {
        Instant from = Instant.parse("2026-01-01T00:00:00Z");
        Instant to = Instant.now();

        when(coinRepository.findById(1L)).thenReturn(Optional.of(testCoin));
        lenient().when(coinGeckoClient.getCoinMarketChartRange("bitcoin", from, to))
                .thenThrow(new RuntimeException("Runtime exception: the expected exception to handle gracefully"));
        // Should catch exceptions and handle them internally
        assertDoesNotThrow(() -> syncService.syncMissingRange(1L, from, to));
        verify(coinRepository).findById(1L);
        verify(coinGeckoClient).getCoinMarketChartRange("bitcoin", from, to);
    }

    @Test
    void shouldSyncSuccessfullyWhenDataExists() {
        // TODO: change this to check that the update time is truncated to 5 minutes
        // if there is already a saved value at this time don't save

        /*Instant from = Instant.parse("2026-01-01T00:00:00Z");
        Instant to = Instant.parse("2026-01-05T00:00:00Z");

        when(coinRepository.findById(1L)).thenReturn(Optional.of(testCoin));

        CoinGeckoMarketChartDto chartDto = new CoinGeckoMarketChartDto();
        List<Object> list = List.of(
                1L,
                BigDecimal.valueOf(1.1)
        );
        CoinGeckoChartPointDto pointDto = new CoinGeckoChartPointDto(list);
        chartDto.setPrices(List.of(pointDto));
        when(coinGeckoClient.getCoinMarketChartRange("bitcoin", from, to))
                .thenReturn(chartDto);

        when(repository.findByCoinIdLastUpdated(eq(1L), any(Instant.class)))
                .thenReturn(null);
        when(repository.findFirstByCoinIdOrderByLastUpdatedDesc(1L))
                .thenReturn(null);

        assertDoesNotThrow(() -> syncService.syncMissingRange(1L, from, to));

        verify(coinRepository).findById(1L);
        verify(coinGeckoClient).getCoinMarketChartRange("bitcoin", from, to);*/
        //verify(repository, atLeastOnce()).findByCoinIdLastUpdated(eq(1L), any(Instant.class));
    }

}
