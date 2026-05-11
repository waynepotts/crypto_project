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


    public void syncCoins() {
        int page = 1;
        final int pageSize = 250;
        boolean nextPage = true;
        do{
            nextPage = processPage(page, pageSize);
            page++;
        } while(nextPage);
    }

    //@Transactional
    public boolean processPage(int page, int pageSize) {
        boolean nextPage = true;
        logger.info("Starting CoinGecko sync page " + page);
        List<CoinGeckoCoinDto> coins =
                coinGeckoClient.getMarkets(page, pageSize);
        nextPage = (!coins.isEmpty()) && page < 50;
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
                    CoinMarketData lastData = coinMarketDataRepository.findFirstByCoinIdOrderByLastUpdatedDesc(coin.getId());
                    if (!lastData.getLastUpdated().equals(dto.getLastUpdated())) {
                        CoinMarketData coinData = CoinMarketDataMapper.fromDto(dto);
                        coinData.setCoin(coin);
                        coinMarketDataRepository.save(coinData);
                    } else {
                        //logger.info("CoinMarketData for coin with id " + coin.getSymbol() + " not updated");
                    }
                } else {
                    CoinMarketData coinData = CoinMarketDataMapper.fromDto(dto);
                    coinData.setCoin(coin);
                    coinMarketDataRepository.save(coinData);
                }
            } catch (Exception e) {
                logger.error("coin market data for " + dto.getId() + " not saved " + e.getMessage() + ", " + dto.toString());
                nextPage = false;
                break;
            }
        }
        return nextPage;
    }
}

