package com.wayne.restservices.validators;

import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.exceptions.CoinAlreadyExistsException;
import com.wayne.restservices.repositories.CoinRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinValidatorTest {

    @Mock
    private CoinRepository coinRepository;

    @InjectMocks
    private CoinValidator coinValidator;

    private CreateCoinRequestDto request;

    @BeforeEach
    void setUp() {

        request = new CreateCoinRequestDto();

        request.setCoingeckoId("bitcoin");
        request.setSymbol("BTC");
        request.setName("Bitcoin");
    }

    @Test
    void shouldPassValidationWhenCoinDoesNotExist() {

        when(coinRepository.existsByCoingeckoId(
                "bitcoin"))
                .thenReturn(false);

        when(coinRepository.existsBySymbolIgnoreCase(
                "BTC"))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                coinValidator.validateCreateCoin(
                        request));

        verify(coinRepository)
                .existsByCoingeckoId("bitcoin");

        verify(coinRepository)
                .existsBySymbolIgnoreCase("BTC");
    }

    @Test
    void shouldThrowExceptionWhenCoingeckoIdAlreadyExists() {

        when(coinRepository.existsByCoingeckoId(
                "bitcoin"))
                .thenReturn(true);

        CoinAlreadyExistsException exception =
                assertThrows(
                        CoinAlreadyExistsException.class,
                        () -> coinValidator
                                .validateCreateCoin(request)
                );

        assertEquals(
                "Coin already exists with CoinGecko ID: bitcoin",
                exception.getMessage());

        verify(coinRepository)
                .existsByCoingeckoId("bitcoin");

        verify(coinRepository, never())
                .existsBySymbolIgnoreCase(anyString());
    }

    @Test
    void shouldThrowExceptionWhenSymbolAlreadyExists() {

        when(coinRepository.existsByCoingeckoId(
                "bitcoin"))
                .thenReturn(false);

        when(coinRepository.existsBySymbolIgnoreCase(
                "BTC"))
                .thenReturn(true);

        CoinAlreadyExistsException exception =
                assertThrows(
                        CoinAlreadyExistsException.class,
                        () -> coinValidator
                                .validateCreateCoin(request)
                );
        verify(coinRepository)
                .existsByCoingeckoId("bitcoin");

        verify(coinRepository)
                .existsBySymbolIgnoreCase("BTC");
    }
}