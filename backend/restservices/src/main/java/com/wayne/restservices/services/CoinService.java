package com.wayne.restservices.services;

import com.wayne.restservices.entities.dto.CoinResponseDto;
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



    public List<CoinResponseDto> getAllCoins() {
        List<CoinResponseDto> allCoins;
        try {
            allCoins = coinRepository.findAll()
                    .stream()
                    .map(CoinMapper::toDto)
                    .toList();
        } catch (Exception ex) {
            log.error("Failed to to get all coins", ex);
            throw ex;
        }
        return allCoins;
    }

    public CoinResponseDto getCoin(Long id) {
        CoinResponseDto coin;
        try {
            coin = coinRepository.findById(id)
                    .map(CoinMapper::toDto)
                    .orElseThrow();
        } catch (Exception e) {
            log.error("Failed to to get coin " + id, e);
            throw e;
        }
        return coin;
    }
}
