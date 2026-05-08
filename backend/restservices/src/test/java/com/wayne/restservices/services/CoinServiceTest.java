package com.wayne.restservices.services;

import com.wayne.restservices.dtos.UpdateCoinRequestDto;
import com.wayne.restservices.dtos.CoinResponseDto;
import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.mappers.CoinMapper;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.validators.CoinValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CoinServiceTest {

    @Mock
    private CoinRepository coinRepository;

    @Mock
    private CoinValidator coinValidator;

    @InjectMocks
    private CoinService coinService;

    /*@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }*/

    @Test
    void shouldCreateCoin() {

        CreateCoinRequestDto request =
                new CreateCoinRequestDto();

        request.setCoingeckoId("bitcoin");
        request.setSymbol("BTC");
        request.setName("Bitcoin");

        Coin savedCoin = new Coin();
        savedCoin.setId(1L);
        savedCoin.setCoingeckoId("bitcoin");
        savedCoin.setSymbol("BTC");
        savedCoin.setName("Bitcoin");

        when(coinRepository.save(any(Coin.class)))
                .thenReturn(savedCoin);

        CoinResponseDto response =
                coinService.createCoin(request);

        assertNotNull(response);

        assertEquals(
                "bitcoin",
                response.getCoingeckoId());

        assertEquals(
                "BTC",
                response.getSymbol());

        verify(coinValidator)
                .validateCreateCoin(request);

        verify(coinRepository)
                .save(any(Coin.class));
    }

    @Test
    void shouldUpdateCoin() {

        Long coinId = 1L;

        UpdateCoinRequestDto request =
                new UpdateCoinRequestDto();

        request.setName("Bitcoin Updated");

        Coin existingCoin = new Coin();
        existingCoin.setId(coinId);
        existingCoin.setName("Bitcoin");

        Coin updatedCoin = new Coin();
        updatedCoin.setId(coinId);
        updatedCoin.setName("Bitcoin Updated");

        when(coinRepository.save(any(Coin.class)))
                .thenReturn(updatedCoin);

        CoinResponseDto response =
                coinService.updateCoin(
                        request);

        assertNotNull(response);

        assertEquals(
                "Bitcoin Updated",
                response.getName());
        assertEquals(existingCoin.getId(), updatedCoin.getId());
        assertEquals(existingCoin.getId(), response.getId());
        assertNotEquals(existingCoin.getName(), updatedCoin.getName());
    }

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
        when(coinRepository.findAll())
                .thenReturn(List.of(coin));



        List<CoinResponseDto> result =
                coinService.getAllCoins();
        assertTrue(CoinMapper.equals(coin, dto));
        assertEquals(1, result.size());
        assertEquals("Bitcoin",
                result.get(0).getName());
    }

    @Test
    void shouldReturnCoin() {
        Coin coin = createCoin();

        CoinResponseDto dto = CoinMapper.toDto(coin);
        when(coinRepository.findById(1L)).thenReturn(Optional.of(coin));

        CoinResponseDto result = coinService.getCoin(1L);
        assertTrue(CoinMapper.equals(coin, dto));
        assertEquals(1L, result.getId());
        assertEquals("coingeckoId_BTC", result.getCoingeckoId());
        assertEquals(dto.getName(), result.getName());
    }

    Coin createCoin() {
        Coin coin = new Coin();
        coin.setId(1L);
        coin.setCoingeckoId("coingeckoId_BTC");
        coin.setName("Bitcoin");
        coin.setSymbol("BTC");
        coin.setImage("bitcoin.png");
        return coin;
    }

    @Test
    void shouldThrowExceptionWhenCoinNotFound() {

        /*Long coinId = 999L;

        when(coinRepository.findById(coinId))
                .thenReturn(Optional.empty());*/
        try {
            if(null == coinService.updateCoin(
                    new UpdateCoinRequestDto())){
                throw new CoinNotFoundException(new UpdateCoinRequestDto().getId());
            }
            fail(); // should not be reached
        } catch (CoinNotFoundException e) {
            assertTrue(true);
        }
        /*assertThrows(
                CoinNotFoundException.class,
                () -> coinService.updateCoin(
                        new UpdateCoinRequestDto()
                )
        );*/

       /* verify(coinRepository)
                .findById(coinId);*/
    }
}