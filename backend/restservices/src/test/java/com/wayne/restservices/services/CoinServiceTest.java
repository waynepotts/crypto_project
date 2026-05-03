package com.wayne.restservices.services;

import com.wayne.restservices.entities.dto.CoinResponseDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoinServiceTest {

    @Mock
    private CoinRepository repository;

    @Mock
    private CoinMapper mapper;

    @InjectMocks
    private CoinService service;

    @Test
    void shouldReturnAllCoins() {

        Coin coin = new Coin();
        coin.setId(1L);
        coin.setName("Bitcoin");

        CoinResponseDto dto = new CoinResponseDto();
        dto.setId(1L);
        dto.setName("Bitcoin");

        when(repository.findAll())
                .thenReturn(List.of(coin));

        when(mapper.toDto(coin))
                .thenReturn(dto);

        List<CoinResponseDto> result =
                service.getAllCoins();

        assertEquals(1, result.size());
        assertEquals("Bitcoin",
                result.get(0).getName());
    }
}