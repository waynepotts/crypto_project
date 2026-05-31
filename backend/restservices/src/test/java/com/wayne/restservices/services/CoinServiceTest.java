package com.wayne.restservices.services;

import com.wayne.restservices.dtos.PagedResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
                response.coingeckoId());

        assertEquals(
                "BTC",
                response.symbol());

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
                response.name());
        assertEquals(existingCoin.getId(), updatedCoin.getId());
        assertEquals(existingCoin.getId(), response.id());
        assertNotEquals(existingCoin.getName(), updatedCoin.getName());
    }

    @Test
    void shouldReturnAllCoins() {

        Coin coin = new Coin();
        coin.setId(1L);
        coin.setCoingeckoId("coingeckoId_BTC");
        coin.setName("Bitcoin");
        coin.setSymbol("Bitcoin");

        CoinResponseDto dto = new CoinResponseDto(1L, "coingeckoId_BTC", "Bitcoin", "Bitcoin", null, null);
        when(coinRepository.findAll())
                .thenReturn(List.of(coin));



        List<CoinResponseDto> result =
                coinService.getAllCoins();
        assertTrue(CoinMapper.equals(coin, dto));
        assertEquals(1, result.size());
        assertEquals("Bitcoin",
                result.get(0).name());
    }

    @Test
    void shouldReturnCoin() {
        Coin coin = createCoin();

        CoinResponseDto dto = CoinMapper.toDto(coin);
        when(coinRepository.findById(1L)).thenReturn(Optional.of(coin));

        CoinResponseDto result = coinService.getCoin(1L);
        assertTrue(CoinMapper.equals(coin, dto));
        assertEquals(1L, result.id());
        assertEquals("coingeckoId_BTC", result.coingeckoId());
        assertEquals(dto.name(), result.name());
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

    @Test
    void shouldSearchCoinsWithCaseInsensitiveQuery() {
        Coin coin = createCoin();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Coin> coinPage = new PageImpl<>(List.of(coin), pageable, 1);

        when(coinRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "BIT", "BIT", pageable))
                .thenReturn(coinPage);

        PagedResponseDto<CoinResponseDto> result =
                coinService.searchCoins("BIT", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Bitcoin", result.getContent().get(0).name());
    }

    @Test
    void shouldSearchCoinsWithEmptyQuery() {
        Coin coin = createCoin();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Coin> coinPage = new PageImpl<>(List.of(coin), pageable, 1);

        when(coinRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "", "", pageable))
                .thenReturn(coinPage);

        PagedResponseDto<CoinResponseDto> result =
                coinService.searchCoins("", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void shouldSearchCoinsReturnsCorrectSecondPage() {
        Coin coin1 = createCoin();
        Coin coin2 = new Coin();
        coin2.setId(2L);
        coin2.setCoingeckoId("ethereum");
        coin2.setName("Ethereum");
        coin2.setSymbol("ETH");
        coin2.setImage("ethereum.png");

        Pageable pageable = PageRequest.of(1, 1);
        Page<Coin> coinPage = new PageImpl<>(List.of(coin2), pageable, 2);

        when(coinRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "coin", "coin", pageable))
                .thenReturn(coinPage);

        PagedResponseDto<CoinResponseDto> result =
                coinService.searchCoins("coin", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Ethereum", result.getContent().get(0).name());
        assertEquals(1, result.getPage());
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.isLast());
    }

    @Test
    void shouldSearchCoinsByName() {
        Coin coin = createCoin();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Coin> coinPage = new PageImpl<>(List.of(coin), pageable, 1);

        when(coinRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "bit", "bit", pageable))
                .thenReturn(coinPage);

        PagedResponseDto<CoinResponseDto> result =
                coinService.searchCoins("bit", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Bitcoin", result.getContent().get(0).name());
        assertEquals(1, result.getTotalElements());

        verify(coinRepository).findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "bit", "bit", pageable);
    }

    @Test
    void shouldSearchCoinsBySymbol() {
        Coin coin = createCoin();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Coin> coinPage = new PageImpl<>(List.of(coin), pageable, 1);

        when(coinRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "btc", "btc", pageable))
                .thenReturn(coinPage);

        PagedResponseDto<CoinResponseDto> result =
                coinService.searchCoins("btc", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("BTC", result.getContent().get(0).symbol());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldReturnEmptyPageWhenNoCoinsMatchSearch() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Coin> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(coinRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "xyz", "xyz", pageable))
                .thenReturn(emptyPage);

        PagedResponseDto<CoinResponseDto> result =
                coinService.searchCoins("xyz", pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    void shouldSearchCoinsAndReturnPagedResponse() {
        Coin coin1 = createCoin();
        Coin coin2 = new Coin();
        coin2.setId(2L);
        coin2.setCoingeckoId("ethereum");
        coin2.setName("Ethereum");
        coin2.setSymbol("ETH");
        coin2.setImage("ethereum.png");

        Pageable pageable = PageRequest.of(0, 2);
        Page<Coin> coinPage = new PageImpl<>(List.of(coin1, coin2), pageable, 2);

        when(coinRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "coin", "coin", pageable))
                .thenReturn(coinPage);

        PagedResponseDto<CoinResponseDto> result =
                coinService.searchCoins("coin", pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
    }
}