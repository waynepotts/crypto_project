package com.wayne.restservices.services;

import com.wayne.restservices.clients.CoinGeckoClient;
import com.wayne.restservices.config.cache.CacheNames;
import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.CoinHistoryPagedResponseDto;
import com.wayne.restservices.dtos.CoinHistoryResponseDto;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoExchangeResponseDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.jobs.events.CoinMarketDataSyncRequestEvent;
import com.wayne.restservices.mappers.CoinMapper;
import com.wayne.restservices.mappers.CoinMarketDataMapper;
import com.wayne.restservices.repositories.CoinMarketDataRepository;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.utils.ChronoUnitConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoinMarketDataService {

    Logger log = LoggerFactory.getLogger(CoinMarketDataService.class);
    private final CoinRepository coinRepository;
    private final CoinMarketDataRepository coinMarketDataRepository;
    private final CoinGeckoClient coinGeckoClient;
    private final ApplicationEventPublisher publisher;
    public CoinMarketDataService(CoinRepository coinRepository, CoinMarketDataRepository coinMarketDataRepository, CoinGeckoClient coinGeckoClient, ApplicationEventPublisher applicationEventPublisher) {
        this.coinRepository = coinRepository;
        this.coinMarketDataRepository = coinMarketDataRepository;
        this.coinGeckoClient = coinGeckoClient;
        this.publisher = applicationEventPublisher;
    }

    public CoinHistoryPagedResponseDto getCoinHistoryPaged(Long id, Instant from, Instant to, int pageNumber, int pageSize) {
        return getCoinHistoryPaged( id, from, to, ChronoUnit.HOURS, pageNumber,  pageSize);
    }
    @Cacheable(
            value = CacheNames.MARKET_DATA,
            key = "#coinId + ':' + #from + ':' + #to + ':' + #granularity",
            unless = "#result.completeness() < 0.95"
    )
    public CoinHistoryResponseDto getCoinHistory(Long coinId, Instant from, Instant to, ChronoUnit granularity) {
        Coin coin = coinRepository.findById(coinId).orElseThrow(()->new CoinNotFoundException(coinId));
        List<CoinHistoryPointDto> list = coinMarketDataRepository.findByCoinCreatedAtRange(coin,from, to, granularity).stream().map(CoinMarketDataMapper::toDto).toList();
        CoinHistoryResponseDto result = CoinMarketDataMapper.fromList(list, CoinMapper.toDto(coin),  from, to, granularity);

        if(result.completeness() < 0.95d) {
            log.info("completeness {} {} {}", coin.getSymbol(), result.completeness(), result.chartData().size());
            publisher.publishEvent(new CoinMarketDataSyncRequestEvent(coinId, from, to));
        }
        return result;
    }

    public CoinHistoryPagedResponseDto getCoinHistoryPaged(Long coinId, Instant from, Instant to, ChronoUnit granularity, int pageNumber, int pageSize) {
        Coin coin = coinRepository.findById(coinId).orElseThrow(()->new CoinNotFoundException(coinId));
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("lastUpdated").ascending()
        );
        Page<CoinHistoryPointDto> page = coinMarketDataRepository.findByCoinLastUpdatedRangePaged(coin, from, to, granularity, pageable).map(CoinMarketDataMapper::toDto);
        CoinHistoryPagedResponseDto retPage = new CoinHistoryPagedResponseDto(page);
        retPage.setCoinName(coin.getName());
        retPage.setCoinId(coin.getId());
        if(page.getSize() == 0){
            publisher.publishEvent(new CoinMarketDataSyncRequestEvent(coin.getId(),from, to ));
        }
        return retPage;
    }

    @Transactional
    public void syncCoins() {
        int page = 1;
        final int pageSize = 250;
        List<CoinGeckoCoinDto> coins =
                coinGeckoClient.getMarkets(page, pageSize);
        List<String> notUpdated = new ArrayList<>();
        for (CoinGeckoCoinDto dto : coins) {
            try {
                Coin coin =
                        coinRepository
                                .findByCoingeckoId(dto.getId())
                                .orElse(new Coin());
                boolean newCoin = coin.getId() == null;
                coin.setSymbol(dto.getSymbol());
                coin.setName(dto.getName());
                coin.setCoingeckoId(dto.getId());
                coin.setImage(dto.getImage());
                coin = coinRepository.save(coin);
                if (!newCoin) {
                    CoinMarketData lastData = coinMarketDataRepository.findFirstByCoinIdOrderByGranularTimestampDesc(coin.getId());
                    if (lastData != null && !lastData.getLastUpdated().equals(dto.getLastUpdated())) {
                        CoinMarketData coinData = CoinMarketDataMapper.fromDto(dto);
                        coinData.setCoin(coin);
                        coinMarketDataRepository.save(coinData);
                    } else {
                        notUpdated.add(coin.getSymbol());

                    }
                } else {
                    CoinMarketData coinData = CoinMarketDataMapper.fromDto(dto);
                    coinData.setCoin(coin);
                    coinMarketDataRepository.save(coinData);
                }
            } catch (Exception e) {
                log.error("unable to sync coin market data", e);
                break;
            }
        }
        log.info("coins not updated because there is no new data {}", notUpdated);
    }

    @Deprecated
    public CoinHistoryResponseDto getChartData(Long id, Integer days, int chrono){
        Instant now = ChronoUnitConverter.normalizeFiveMinutes(Instant.now());
        Instant last = now.minus(Duration.ofDays(days));
        ChronoUnit granularity  = switch (chrono) {
            case 1 -> ChronoUnit.MINUTES;
            case 2 -> ChronoUnit.HOURS;
            default -> ChronoUnit.DAYS;
        };

        int page = 0;
        CoinHistoryPagedResponseDto paged = getCoinHistoryPaged(id, last , now, granularity, page, 500);
        Coin coin =
                coinRepository
                        .findById(id).orElseThrow(()-> new CoinNotFoundException(id));
        CoinHistoryResponseDto found = CoinMarketDataMapper.fromPaged(paged, CoinMapper.toDto(coin),  last, now, granularity);

        if(found.completeness() < 0.95d) {
            log.info("completeness {} {}", coin.getSymbol(), found.completeness());
            publisher.publishEvent(new CoinMarketDataSyncRequestEvent(id, last, now));
        }
        return found;
        /*CoinGeckoClient.Interval interval = daily ? CoinGeckoClient.Interval.daily : CoinGeckoClient.Interval.hourly;

        CoinHistoryResponseDto dto = CoinMarketDataMapper.fromCoinGecko(coinGeckoClient.getCoinMarketChart(coin.getCoingeckoId(), days, interval));
        return new CoinHistoryResponseDto(dto.chartData(), dto.completeness(), CoinMapper.toDto(coin));*/
    }

    /**
     * returns the most recently created coin market data for the ranks between the params
     * @param marketCapRankStart
     * @param marketCapRankEnd
     * @return dto objects for the data, max results 250
     */
    @Cacheable(
            value = CacheNames.MARKET_CAP,
            key = "'cap'"
    )
    public List<CoinMarketDataDto> getMarketDataByMarketCapRankRange(Integer marketCapRankStart, Integer marketCapRankEnd) {
        //Pageable pageable = PageRequest.of(0, marketCapRankEnd, Sort.by("marketCapRank").ascending());
        return coinMarketDataRepository
                .findLatestMarketCapRankRange(marketCapRankStart, marketCapRankEnd, Math.min(250, marketCapRankEnd - marketCapRankStart))
                .stream().map(CoinMarketDataMapper::toMarketDataDto)
                .toList();//.collect(Collectors.toList());
    }

    @Cacheable(
            value = CacheNames.EXCHANGE_RATES,
            key = "'rates'"
    )
    public CoinGeckoExchangeResponseDto getExchangeRates(){
        return coinGeckoClient.getExchangeRates();
    }


}
