package com.wayne.restservices.entities.mapper;

import com.wayne.restservices.entities.dto.CoinResponseDto;
import com.wayne.restservices.entities.jpa.Coin;

public class CoinMapper {

    public static CoinResponseDto toDto(Coin coin) {
        CoinResponseDto dto = new CoinResponseDto();
        dto.setId(coin.getId());
        dto.setCoingeckoId(coin.getCoingeckoId());
        dto.setSymbol(coin.getSymbol());
        dto.setName(coin.getName());
        dto.setImage(coin.getImage());
        return dto;
    }

    public static Coin toEntity(CoinResponseDto dto) {
        Coin coin = new Coin();
        coin.setId(dto.getId());
        coin.setCoingeckoId(dto.getCoingeckoId());
        coin.setSymbol(dto.getSymbol());
        coin.setName(dto.getName());
        coin.setImage(dto.getImage());
        return coin;
    }
}
