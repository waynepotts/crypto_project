package com.wayne.restservices.services;

import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.mapper.CoinMapper;
import com.wayne.restservices.repositories.CoinRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoinServiceTest {

    @Mock
    private CoinRepository repository;

    @InjectMocks
    private CoinService service;

    @Test
    void shouldReturnAllCoins() {

        Coin coin = new Coin();
        coin.setId(1L);
        coin.setCoingeckoId("coingeckoId_BTC");
        coin.setName("Bitcoin");
        coin.setSymbol("Bitcoin");

        CoinResponseDto dto = new CoinResponseDto();
        dto.setId(1L);
        dto.setCoingeckoId("coingeckoId_BTC");
        dto.setName("Bitcoin");
        dto.setSymbol("Bitcoin");
        when(repository.findAll())
                .thenReturn(List.of(coin));



        List<CoinResponseDto> result =
                service.getAllCoins();
        assertTrue(CoinMapper.equals(coin, dto));
        assertEquals(1, result.size());
        assertEquals("Bitcoin",
                result.get(0).getName());
    }
}