package com.wayne.restservices.services;

import com.wayne.restservices.dtos.PagedResponseDto;
import com.wayne.restservices.dtos.UpdateCoinRequestDto;
import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.mappers.CoinMapper;
import com.wayne.restservices.validators.CoinValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinService {

    private final CoinRepository coinRepository;
    private final CoinValidator coinValidator;
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
    public CoinService(
            CoinRepository coinRepository,
            CoinValidator coinValidator
    ) {
        this.coinRepository = coinRepository;
        this.coinValidator = coinValidator;
    }

    public PagedResponseDto<CoinResponseDto> getCoins(Pageable pageable) {
        Page<CoinResponseDto> paged = coinRepository
                .findAll(pageable)
                .map(CoinMapper::toDto);
        return new PagedResponseDto<CoinResponseDto>(paged);
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
        Coin coin = coinRepository.findById(id)
                .orElseThrow(() ->
                        new CoinNotFoundException(id));
        return CoinMapper.toDto(coin);
    }

    public CoinResponseDto createCoin(CreateCoinRequestDto request) {
        coinValidator.validateCreateCoin(request);
        Coin coin = CoinMapper.toEntity(request);
        Coin savedCoin =
                coinRepository.save(coin);
        return CoinMapper.toDto(savedCoin);
    }

    public CoinResponseDto updateCoin(UpdateCoinRequestDto updateCoin) {
        coinValidator.validateUpdateCoin(updateCoin);
        Coin coin = CoinMapper.toEntity(updateCoin);
        Coin savedCoin =
                coinRepository.save(coin);
        return CoinMapper.toDto(savedCoin);
    }

    public PagedResponseDto<CoinResponseDto> searchCoins(String query, Pageable pageable) {
        Page<CoinResponseDto> paged = coinRepository
                .findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(query, query, pageable)
                .map(CoinMapper::toDto);
        return new PagedResponseDto<>(paged);
    }
}
