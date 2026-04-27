package com.wayne.entities.mapper;

import com.wayne.entities.dto.CoinDto;
import com.wayne.entities.jpa.Coin;

public class CoinMapper {

    public static CoinDto toDto(Coin coin) {
        CoinDto dto = new CoinDto();
        dto.setId(coin.getId());
        dto.setCoingeckoId(coin.getCoingeckoId());
        dto.setSymbol(coin.getSymbol());
        dto.setName(coin.getName());
        dto.setImage(coin.getImage());
        return dto;
    }

    public static Coin toEntity(CoinDto dto) {
        Coin coin = new Coin();
        coin.setId(dto.getId());
        coin.setCoingeckoId(dto.getCoingeckoId());
        coin.setSymbol(dto.getSymbol());
        coin.setName(dto.getName());
        coin.setImage(dto.getImage());
        return coin;
    }
}
