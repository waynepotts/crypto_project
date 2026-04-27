package com.wayne.restservices.services;

import com.wayne.entities.dto.CoinDto;
import com.wayne.entities.jpa.CoinRepository;
import com.wayne.entities.mapper.CoinMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinService {

    private final CoinRepository coinRepository;

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
