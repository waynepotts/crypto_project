package com.wayne.restservices.facade;

import com.wayne.restservices.dtos.CoinHistoryPagedResponseDto;
import com.wayne.restservices.dtos.CoinHistoryResponseDto;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.jobs.events.CoinMarketDataSyncRequestEvent;
import com.wayne.restservices.mappers.CoinMarketDataMapper;
import com.wayne.restservices.services.CategoryService;
import com.wayne.restservices.services.CoinMarketDataService;
import com.wayne.restservices.services.CoinService;
import com.wayne.restservices.utils.ChronoUnitConverter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/***
 * Facade used to call all things regarding the market data, coins and the categories and unify them into a single
 * interface for controllers to call. The purpose of this class is that it combines the information from services,
 * business logic in this class should be kept to a minimum
 * N.B. Don't let this class become a dumping ground for misc features because you don't know where to put them, treat it
 * like a controller for the abstract concept of coins and the market data.
 */
@Service
public class MarketFacade {

    private final CoinService coinService;
    private final CoinMarketDataService coinMarketDataService;
    private final CategoryService categoryService;
    private final ApplicationEventPublisher publisher;

    public MarketFacade(CoinService coinService, CoinMarketDataService coinMarketDataService, CategoryService categoryService, ApplicationEventPublisher publisher) {
        this.coinService = coinService;
        this.coinMarketDataService = coinMarketDataService;
        this.categoryService = categoryService;
        this.publisher = publisher;
    }

    public CoinHistoryResponseDto getChartData(Long coinId, Integer days, int chronoUnit ) {
        Instant now = ChronoUnitConverter.normalizeFiveMinutes(Instant.now()).plusSeconds(301);
        Instant last = now.minus(Duration.ofDays(days)).minusSeconds(302);
        ChronoUnit granularity  = switch (chronoUnit) {
            case 1 -> ChronoUnit.MINUTES;
            case 2 -> ChronoUnit.HOURS;
            default -> ChronoUnit.DAYS;
        };
        return coinMarketDataService.getCoinHistory(coinId, last , now, granularity);
    }
    public List<CoinMarketDataDto> getMarketCapRankRange(Integer start, Integer end) {
        return coinMarketDataService.getMarketDataByMarketCapRankRange(start, end);
    }
}
