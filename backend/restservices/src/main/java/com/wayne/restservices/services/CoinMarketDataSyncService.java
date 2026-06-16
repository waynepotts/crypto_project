package com.wayne.restservices.services;

import com.wayne.restservices.clients.CoinGeckoClient;
import com.wayne.restservices.dtos.coingecko.CoinGeckoMarketChartDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.jobs.SyncTracker;
import com.wayne.restservices.jobs.events.CoinMarketDataSyncRequestEvent;
import com.wayne.restservices.mappers.CoinMarketDataMapper;
import com.wayne.restservices.repositories.CoinMarketDataRepository;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.utils.ChronoUnitConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
//@RequiredArgsConstructor
public class CoinMarketDataSyncService {

    private final CoinGeckoClient coinGeckoClient;
    private final CoinMarketDataRepository repository;
    private final CoinRepository coinRepository;
    private static final Logger log =
            LoggerFactory.getLogger(CoinMarketDataSyncService.class);
    public CoinMarketDataSyncService(CoinGeckoClient coinGeckoClient, CoinMarketDataRepository repository, CoinRepository coinRepository) {
        this.coinGeckoClient = coinGeckoClient;
        this.repository = repository;
        this.coinRepository = coinRepository;
    }

    public void syncMissingRange(
            Long coinId,
            Instant from,
            Instant to
    ) {
        Coin coin = coinRepository.findById(coinId).orElseThrow(()->new CoinNotFoundException(coinId));
        log.info(
                "Syncing missing market data for {} from {} to {}",
                coin.getSymbol(),
                from,
                to
        );
        if(coin.getId() == null){
            // new coin
            // coin = coinRepository.save(coin);
        }
        try {

            CoinGeckoMarketChartDto response =
                    coinGeckoClient.getCoinMarketChartRange(
                            coin.getCoingeckoId(),
                            from,
                            to
                    );

            List<CoinMarketData> entities = CoinMarketDataMapper.fromHistory(response, coin);
            List<CoinMarketData> saved = new ArrayList<>();
            for(CoinMarketData marketData : entities) {
                //log.info("coin id {}, lastUpdated {}", marketData.getCoin().getId(), marketData.getLastUpdated());

                CoinMarketData lastData = repository.findByCoinIdCreatedAt(coinId, marketData.getCreatedAt());
                if (lastData == null) {
                    marketData = repository.save(marketData);
                    saved.add(marketData);
                }
            }
            // repository.saveAll(entities);

            log.info(
                    "Saved {} records for {}",
                    saved.size(),
                    coin.getSymbol()
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to sync market data for {}",
                    coin.getSymbol(),
                    ex
            );
        }

    }
}
