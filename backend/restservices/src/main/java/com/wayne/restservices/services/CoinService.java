package com.wayne.restservices.services;

import com.wayne.restservices.entities.dto.CoinDto;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.entities.mapper.CoinMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinService {

    private final CoinRepository coinRepository;

    private static final Logger log =
            LoggerFactory.getLogger(CoinService.class);

    public void refreshCoins() {

        log.info("Starting CoinGecko refresh");

        try {

            // your logic here

            log.info("Coin refresh completed successfully");

        } catch (Exception ex) {

            log.error("Failed to refresh coins", ex);

            throw ex;
        }
    }
    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public List<CoinDto> getAllCoins() {
        return coinRepository.findAll()
                .stream()
                .map(CoinMapper::toDto)
                .toList();
    }

    public CoinDto getCoin(Long id) {
        return coinRepository.findById(id)
                .map(CoinMapper::toDto)
                .orElseThrow();
    }
}
