package com.wayne.restservices.services;

import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.CoinHistoryPagedResponseDto;
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

import java.time.Instant;

@Service
public class CoinMarketDataService {

    Logger log = LoggerFactory.getLogger(CoinMarketDataService.class);
    private final CoinRepository coinRepository;
    private final CoinMarketDataRepository coinMarketDataRepository;
    public CoinMarketDataService(CoinRepository coinRepository, CoinMarketDataRepository coinMarketDataRepository) {
        this.coinRepository = coinRepository;
        this.coinMarketDataRepository = coinMarketDataRepository;
    }

    public CoinHistoryPagedResponseDto getCoinHistory(Long id, Instant from, Instant to, int pageNumber, int pageSize) {
        Coin coin = coinRepository.findById(id).orElseThrow(()->new CoinNotFoundException(id));
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("lastUpdated").ascending()
        );
        Page<CoinHistoryPointDto> page = coinMarketDataRepository.findByCoinLastUpdatedRange(coin, from, to, pageable).map(CoinMarketDataMapper::toDto);
        CoinHistoryPagedResponseDto retPage = new CoinHistoryPagedResponseDto(page);
        retPage.setCoinName(coin.getName());
        retPage.setCoinId(coin.getId());
        return retPage;
    }
}
