package com.wayne.restservices.facade;

import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.CoinHistoryResponseDto;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.services.CoinMarketDataService;
import com.wayne.restservices.services.CoinService;
import com.wayne.restservices.services.CategoryService;
import com.wayne.restservices.utils.ChronoUnitConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketFacadeTest {

    @Mock
    private CoinService coinService;

    @Mock
    private CoinMarketDataService coinMarketDataService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private MarketFacade marketFacade;

    @Test
    void shouldGetChartDataWithChronoUnitMinutes() {
        Instant now = ChronoUnitConverter.normalizeFiveMinutes(Instant.now()).plusSeconds(301);
        Instant expectedLast = now.minus(java.time.Duration.ofDays(7)).minusSeconds(302);

        CoinHistoryResponseDto mockResult = createMockCoinHistoryDto();
        when(coinMarketDataService.getCoinHistory(eq(1L), eq(expectedLast), eq(now), eq(ChronoUnit.MINUTES)))
                .thenReturn(mockResult);

        CoinHistoryResponseDto result = marketFacade.getChartData(1L, 7, 1);

        assertNotNull(result);
        assertEquals("Bitcoin", result.coinDto().name());
        // TODO: fix this test so the below assert can be uncommented 
        //assertEquals(List.of(new CoinHistoryPointDto(now, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN)),
         //       result.chartData());
        verify(coinMarketDataService).getCoinHistory(eq(1L), eq(expectedLast), eq(now), eq(ChronoUnit.MINUTES));
    }

    @Test
    void shouldGetChartDataWithChronoUnitHours() {
        CoinHistoryResponseDto mockResult = createMockCoinHistoryDto();
        when(coinMarketDataService.getCoinHistory(anyLong(), any(Instant.class), any(Instant.class), eq(ChronoUnit.HOURS)))
                .thenReturn(mockResult);

        CoinHistoryResponseDto result = marketFacade.getChartData(1L, 30, 2);

        assertNotNull(result);
        verify(coinMarketDataService).getCoinHistory(anyLong(), any(Instant.class), any(Instant.class), eq(ChronoUnit.HOURS));
    }

    @Test
    void shouldGetChartDataWithDefaultDaysGranularity() {
        CoinHistoryResponseDto mockResult = createMockCoinHistoryDto();
        when(coinMarketDataService.getCoinHistory(anyLong(), any(Instant.class), any(Instant.class), eq(ChronoUnit.DAYS)))
                .thenReturn(mockResult);

        CoinHistoryResponseDto result = marketFacade.getChartData(2L, 90, 5);

        assertNotNull(result);
        verify(coinMarketDataService).getCoinHistory(eq(2L), any(Instant.class), any(Instant.class), eq(ChronoUnit.DAYS));
    }

    @Test
    void shouldGetChartDataWithSingleDay() {
        CoinHistoryResponseDto mockResult = createMockCoinHistoryDto();
        when(coinMarketDataService.getCoinHistory(eq(3L), any(Instant.class), any(Instant.class), eq(ChronoUnit.MINUTES)))
                .thenReturn(mockResult);

        CoinHistoryResponseDto result = marketFacade.getChartData(3L, 1, 1);

        assertNotNull(result);
        verify(coinMarketDataService).getCoinHistory(eq(3L), any(Instant.class), any(Instant.class), eq(ChronoUnit.MINUTES));
    }

    @Test
    void shouldGetMarketCapRankRange() {
        CoinMarketDataDto marketDataDto = new CoinMarketDataDto(1L, 1L, "Bitcoin", "BTC", BigDecimal.valueOf(50000),
                BigDecimal.valueOf(1000000000), 1, Instant.now(), BigDecimal.valueOf(100));
        when(coinMarketDataService.getMarketDataByMarketCapRankRange(1, 5))
                .thenReturn(List.of(marketDataDto));

        List<CoinMarketDataDto> result = marketFacade.getMarketCapRankRange(1, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
        assertEquals(1, result.get(0).marketCapRank());
        verify(coinMarketDataService).getMarketDataByMarketCapRankRange(1, 5);
    }

    @Test
    void shouldGetMarketCapRankRangeForSingleCoin() {
        CoinMarketDataDto marketDataDto = new CoinMarketDataDto(1L, 1L, "Bitcoin", "BTC", BigDecimal.valueOf(50000),
                BigDecimal.valueOf(1000000000), 1, Instant.now(), BigDecimal.valueOf(100));
        when(coinMarketDataService.getMarketDataByMarketCapRankRange(1, 1))
                .thenReturn(List.of(marketDataDto));

        List<CoinMarketDataDto> result = marketFacade.getMarketCapRankRange(1, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
    }

    private CoinHistoryResponseDto createMockCoinHistoryDto() {
        List<CoinHistoryPointDto> chartData = List.of(new CoinHistoryPointDto(Instant.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN));
        CoinResponseDto coinDto = new CoinResponseDto(1L, "bitcoin", "BTC", "Bitcoin", "bitcoin.png", null);

        List<String> marketCategories = List.of("cryptocurrency");
        CoinResponseDto fullCoinDto = new CoinResponseDto(1L, "bitcoin", "BTC", "Bitcoin", "bitcoin.png", marketCategories);

        return new CoinHistoryResponseDto(chartData, 0.95, fullCoinDto);
    }
}
