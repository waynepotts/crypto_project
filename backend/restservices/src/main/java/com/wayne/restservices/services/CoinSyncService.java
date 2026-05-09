package com.wayne.restservices.services;

import com.wayne.restservices.clients.CoinGeckoClient;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import com.wayne.restservices.mappers.CoinMarketDataMapper;
import com.wayne.restservices.repositories.CoinMarketDataRepository;
import com.wayne.restservices.repositories.CoinRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.wayne.restservices.mappers.CoinMarketDataMapper.fromDto;

@Service
public class CoinSyncService {

    private final CoinGeckoClient coinGeckoClient;

    private final CoinRepository coinRepository;

    private final CoinMarketDataRepository coinMarketDataRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(
                    CoinSyncService.class);
    public CoinSyncService(
            CoinGeckoClient coinGeckoClient,
            CoinRepository coinRepository,
            CoinMarketDataRepository coinMarketDataRepository) {
        this.coinGeckoClient = coinGeckoClient;
        this.coinRepository = coinRepository;
        this.coinMarketDataRepository = coinMarketDataRepository;
    }

    @Transactional
    public void syncCoins() {
        logger.info("Starting CoinGecko sync");
        List<CoinGeckoCoinDto> coins =
                coinGeckoClient.getMarkets();
        final String urlSource = "/coins/markets";
        for (CoinGeckoCoinDto dto : coins) {

            Coin coin =
                    coinRepository
                            .findByCoingeckoId(dto.getId())
                            .orElse(new Coin());

            coin.setSymbol(dto.getSymbol());
            coin.setName(dto.getName());
            coin.setCoingeckoId(dto.getId());
            coin = coinRepository.save(coin);
            CoinMarketData coinData = CoinMarketDataMapper.fromDto(dto);
            coinData.setCoin(coin);
            coinData.setSource(urlSource);
            coinMarketDataRepository.save(coinData);
        }
    }
}