package com.wayne.restservices.mappers;

import com.wayne.restservices.dtos.UpdateCoinRequestDto;
import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.services.CoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinMapper {

    private static final Logger log =
            LoggerFactory.getLogger(CoinMapper.class);
    public static CoinResponseDto toDto(Coin coin) {
        CoinResponseDto dto = new CoinResponseDto();
        try {


            dto.setId(coin.getId());
            dto.setCoingeckoId(coin.getCoingeckoId());
            dto.setSymbol(coin.getSymbol());
            dto.setName(coin.getName());
            dto.setImage(coin.getImage());
        } catch(NullPointerException e) {
            log.debug("NPE while converting coin to DTO, " + e.getMessage());
            dto = null;
        }
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
    public static Coin toEntity(UpdateCoinRequestDto dto) {
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
