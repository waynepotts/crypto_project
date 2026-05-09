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
        int counter = 0;
        for (CoinGeckoCoinDto dto : coins) {

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
                CoinMarketData lastData = coinMarketDataRepository.findFirstByCoinIdOrderByLastUpdatedDesc(coin.getId());
                if (!lastData.getLastUpdated().equals(dto.getLastUpdated())) {
                    CoinMarketData coinData = CoinMarketDataMapper.fromDto(dto);
                    coinData.setCoin(coin);
                    coinMarketDataRepository.save(coinData);
                    counter++;
                } else{
                    logger.info("CoinMarketData for coin with id " + coin.getSymbol() + " not updated");
                }
            } else{
                CoinMarketData coinData = CoinMarketDataMapper.fromDto(dto);
                coinData.setCoin(coin);
                coinMarketDataRepository.save(coinData);
                counter++;
            }
        }
        logger.info("CoinGecko sync completed, " + counter + " of " + coins.size() + " coins saved");
    }
}