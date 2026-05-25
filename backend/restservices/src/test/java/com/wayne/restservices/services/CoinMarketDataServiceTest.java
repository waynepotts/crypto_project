package com.wayne.restservices.services;

import com.wayne.restservices.clients.CoinGeckoClient;
import com.wayne.restservices.dtos.CoinHistoryPagedResponseDto;
import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoMarketChartDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.repositories.CoinMarketDataRepository;
import com.wayne.restservices.repositories.CoinRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinMarketDataServiceTest {

    @Mock
    private CoinRepository coinRepository;

    @Mock
    private CoinMarketDataRepository coinMarketDataRepository;

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @InjectMocks
    private CoinMarketDataService coinMarketDataService;

    @Test
    void shouldGetCoinHistory() {
        Coin coin = createCoin();
        Instant from = Instant.parse("2026-01-01T00:00:00Z");
        Instant to = Instant.parse("2026-05-25T00:00:00Z");

        CoinMarketData marketData = new CoinMarketData();
        marketData.setCurrentPrice(BigDecimal.valueOf(50000));
        marketData.setMarketCap(BigDecimal.valueOf(1000000000L));
        marketData.setTotalVolume(BigDecimal.valueOf(50000000L));
        marketData.setLastUpdated(Instant.parse("2026-05-24T12:00:00Z"));

        Page<CoinMarketData> dataPage = new PageImpl<>(List.of(marketData));

        when(coinRepository.findById(1L)).thenReturn(Optional.of(coin));
        when(coinMarketDataRepository.findByCoinLastUpdatedRange(
                eq(coin), eq(from), eq(to), eq(ChronoUnit.HOURS), any(PageRequest.class)))
                .thenReturn(dataPage);

        CoinHistoryPagedResponseDto result =
                coinMarketDataService.getCoinHistory(1L, from, to, 0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Bitcoin", result.getCoinName());
        assertEquals(1L, result.getCoinId());
        assertEquals(BigDecimal.valueOf(50000), result.getContent().get(0).getPrice());
    }

    @Test
    void shouldThrowCoinNotFoundExceptionWhenCoinNotFoundInHistory() {
        when(coinRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CoinNotFoundException.class,
                () -> coinMarketDataService.getCoinHistory(
                        999L, Instant.now(), Instant.now(), 0, 20));
    }

    @Test
    void shouldSyncCoinsForNewCoin() {
        CoinGeckoCoinDto dto = new CoinGeckoCoinDto();
        dto.setId("bitcoin");
        dto.setSymbol("BTC");
        dto.setName("Bitcoin");
        dto.setImage("bitcoin.png");
        dto.setCurrentPrice(BigDecimal.valueOf(50000));
        dto.setMarketCap(BigDecimal.valueOf(1000000000L));
        dto.setLastUpdated(Instant.now());

        Coin savedCoin = new Coin();
        savedCoin.setId(1L);
        savedCoin.setCoingeckoId("bitcoin");
        savedCoin.setName("Bitcoin");
        savedCoin.setSymbol("BTC");

        when(coinGeckoClient.getMarkets(1, 250)).thenReturn(List.of(dto));
        when(coinRepository.findByCoingeckoId("bitcoin")).thenReturn(Optional.empty());
        when(coinRepository.save(any(Coin.class))).thenReturn(savedCoin);
        when(coinMarketDataRepository.save(any(CoinMarketData.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        coinMarketDataService.syncCoins();

        verify(coinRepository).findByCoingeckoId("bitcoin");
        verify(coinRepository, times(1)).save(any(Coin.class));
        verify(coinMarketDataRepository).save(any(CoinMarketData.class));
    }

    @Test
    void shouldSyncCoinsForExistingCoinWithNewData() {
        Instant lastUpdated = Instant.now();

        CoinGeckoCoinDto dto = new CoinGeckoCoinDto();
        dto.setId("bitcoin");
        dto.setSymbol("BTC");
        dto.setName("Bitcoin");
        dto.setImage("bitcoin.png");
        dto.setCurrentPrice(BigDecimal.valueOf(51000));
        dto.setMarketCap(BigDecimal.valueOf(1100000000L));
        dto.setLastUpdated(lastUpdated);

        Coin existingCoin = new Coin();
        existingCoin.setId(1L);
        existingCoin.setCoingeckoId("bitcoin");
        existingCoin.setName("Bitcoin");
        existingCoin.setSymbol("BTC");

        CoinMarketData lastData = new CoinMarketData();
        lastData.setLastUpdated(lastUpdated.minusSeconds(3600));

        when(coinGeckoClient.getMarkets(1, 250)).thenReturn(List.of(dto));
        when(coinRepository.findByCoingeckoId("bitcoin")).thenReturn(Optional.of(existingCoin));
        when(coinRepository.save(any(Coin.class))).thenReturn(existingCoin);
        when(coinMarketDataRepository.findFirstByCoinIdOrderByLastUpdatedDesc(1L))
                .thenReturn(lastData);
        when(coinMarketDataRepository.save(any(CoinMarketData.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        coinMarketDataService.syncCoins();

        verify(coinMarketDataRepository).save(any(CoinMarketData.class));
    }

    @Test
    void shouldNotSaveMarketDataForExistingCoinWithoutNewData() {
        Instant lastUpdated = Instant.now();

        CoinGeckoCoinDto dto = new CoinGeckoCoinDto();
        dto.setId("bitcoin");
        dto.setSymbol("BTC");
        dto.setName("Bitcoin");
        dto.setImage("bitcoin.png");
        dto.setCurrentPrice(BigDecimal.valueOf(50000));
        dto.setMarketCap(BigDecimal.valueOf(1000000000L));
        dto.setLastUpdated(lastUpdated);

        Coin existingCoin = new Coin();
        existingCoin.setId(1L);
        existingCoin.setCoingeckoId("bitcoin");
        existingCoin.setName("Bitcoin");
        existingCoin.setSymbol("BTC");

        CoinMarketData lastData = new CoinMarketData();
        lastData.setLastUpdated(lastUpdated);

        when(coinGeckoClient.getMarkets(1, 250)).thenReturn(List.of(dto));
        when(coinRepository.findByCoingeckoId("bitcoin")).thenReturn(Optional.of(existingCoin));
        when(coinRepository.save(any(Coin.class))).thenReturn(existingCoin);
        when(coinMarketDataRepository.findFirstByCoinIdOrderByLastUpdatedDesc(1L))
                .thenReturn(lastData);

        coinMarketDataService.syncCoins();

        verify(coinMarketDataRepository, never()).save(any(CoinMarketData.class));
    }

    @Test
    void shouldGetMarketDataByMarketCapRankRange() {
        Coin coin = createCoin();
        CoinMarketData marketData = new CoinMarketData();
        marketData.setId(1L);
        marketData.setCoin(coin);
        marketData.setCurrentPrice(BigDecimal.valueOf(50000));
        marketData.setMarketCap(BigDecimal.valueOf(1000000000L));
        marketData.setMarketCapRank(1);
        marketData.setLastUpdated(Instant.now());
        marketData.setPriceChange24h(BigDecimal.valueOf(100));

        when(coinMarketDataRepository.findLatestMarketCapRankRange(1, 5, 4))
                .thenReturn(List.of(marketData));

        List<CoinMarketDataDto> result =
                coinMarketDataService.GetMarketDataByMarketCapRankRange(1, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCoinId());
        assertEquals("Bitcoin", result.get(0).getName());
        assertEquals("BTC", result.get(0).getSymbol());
        assertEquals(BigDecimal.valueOf(50000), result.get(0).getCurrentPrice());
        assertEquals(1, result.get(0).getMarketCapRank());
    }

    private Coin createCoin() {
        Coin coin = new Coin();
        coin.setId(1L);
        coin.setCoingeckoId("bitcoin");
        coin.setName("Bitcoin");
        coin.setSymbol("BTC");
        coin.setImage("bitcoin.png");
        return coin;
    }
}
