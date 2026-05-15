package com.wayne.restservices.services;

import com.wayne.restservices.clients.CoinGeckoClient;
import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.CoinHistoryPagedResponseDto;
import com.wayne.restservices.dtos.CoinHistoryResponseDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.mappers.CoinMarketDataMapper;
import com.wayne.restservices.repositories.CoinMarketDataRepository;
import com.wayne.restservices.repositories.CoinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CoinMarketDataService {

    Logger log = LoggerFactory.getLogger(CoinMarketDataService.class);
    private final CoinRepository coinRepository;
    private final CoinMarketDataRepository coinMarketDataRepository;
    private final CoinGeckoClient coinGeckoClient;
    public CoinMarketDataService(CoinRepository coinRepository, CoinMarketDataRepository coinMarketDataRepository, CoinGeckoClient coinGeckoClient) {
        this.coinRepository = coinRepository;
        this.coinMarketDataRepository = coinMarketDataRepository;
        this.coinGeckoClient = coinGeckoClient;
    }

    public CoinHistoryPagedResponseDto getCoinHistory(Long id, Instant from, Instant to, int pageNumber, int pageSize) {
        Coin coin = coinRepository.findById(id).orElseThrow(()->new CoinNotFoundException(id));
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("lastUpdated").ascending()
        );
        Page<CoinHistoryPointDto> page = coinMarketDataRepository.findByCoinLastUpdatedRange(coin, from, to, (short)2, pageable).map(CoinMarketDataMapper::toDto);
        CoinHistoryPagedResponseDto retPage = new CoinHistoryPagedResponseDto(page);
        retPage.setCoinName(coin.getName());
        retPage.setCoinId(coin.getId());
        return retPage;
    }

    @Transactional
    public void syncCoins() {
        int page = 1;
        final int pageSize = 250;
        List<CoinGeckoCoinDto> coins =
                coinGeckoClient.getMarkets(page, pageSize);
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

                break;
            }
        }
    }

    public CoinHistoryResponseDto getChartData(Long id, Integer days, Boolean daily){
        Instant now = Instant.now();
        Instant last = now;
        double expected = days / (daily ? 1.0d: 24.0d);
        int page = 0;
        CoinHistoryPagedResponseDto paged = getCoinHistory(id, now.minus(Duration.ofDays(days)), now, page, 500);
        CoinHistoryResponseDto found = CoinMarketDataMapper.fromPaged(paged, expected);
        CoinGeckoClient.Interval interval = daily ? CoinGeckoClient.Interval.daily : CoinGeckoClient.Interval.hourly;
        Coin coin =
                coinRepository
                        .findById(id).orElseThrow(()-> new CoinNotFoundException(id));
        return CoinMarketDataMapper.fromCoinGecko(coinGeckoClient.getCoinMarketChart(coin.getCoingeckoId(), days, interval));
    }


}
