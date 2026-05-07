package com.wayne.restservices.entities.mapper;

import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
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

    public static Coin toEntity(CreateCoinRequestDto request) {
        Coin coin = new Coin();
        coin.setSymbol(request.getSymbol());
        coin.setName(request.getName());
        coin.setImage(request.getImage());
        coin.setCoingeckoId(request.getCoingeckoId());
        return coin;
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
    public static boolean equals(Coin coin, CoinResponseDto dto) {
        return  coin != null && dto != null
                && coin.getId() == dto.getId()
                && coin.getCoingeckoId() == dto.getCoingeckoId()
                && coin.getSymbol() == dto.getSymbol()
                && coin.getName() == dto.getName()
                && coin.getImage() == dto.getImage()
                ;
    }
}
