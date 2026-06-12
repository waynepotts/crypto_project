package com.wayne.restservices.validators;

import com.wayne.restservices.dtos.CreateCoinRequestDto;
import com.wayne.restservices.dtos.UpdateCoinRequestDto;
import com.wayne.restservices.exceptions.CoinAlreadyExistsException;
import com.wayne.restservices.exceptions.CoinNotFoundException;
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
                "Coin already exists with CoinGecko ID, or symbol: bitcoin",
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

    @Test
    void shouldPassUpdateValidationWhenCoinExists() {
        UpdateCoinRequestDto updateRequest = new UpdateCoinRequestDto();
        updateRequest.setId(1L);
        updateRequest.setCoingeckoId("bitcoin2");
        updateRequest.setSymbol("BTC2");
        updateRequest.setName("Bitcoin Updated");
        updateRequest.setImage("bitcoin2.png");

        when(coinRepository.existsById(1L)).thenReturn(true);
        when(coinRepository.existsByCoingeckoId("bitcoin2")).thenReturn(false);
        when(coinRepository.existsBySymbolIgnoreCase("BTC2")).thenReturn(false);

        assertDoesNotThrow(() -> coinValidator.validateUpdateCoin(updateRequest));

        verify(coinRepository).existsById(1L);
        verify(coinRepository).existsByCoingeckoId("bitcoin2");
        verify(coinRepository).existsBySymbolIgnoreCase("BTC2");
    }

    @Test
    void shouldThrowExceptionWhenUpdateCoinDoesNotExist() {
        UpdateCoinRequestDto updateRequest = new UpdateCoinRequestDto();
        updateRequest.setId(999L);
        updateRequest.setCoingeckoId("ethereum");
        updateRequest.setSymbol("ETH");
        updateRequest.setName("Ethereum");
        updateRequest.setImage("ethereum.png");

        when(coinRepository.existsById(999L)).thenReturn(false);

        CoinNotFoundException exception =
                assertThrows(
                        CoinNotFoundException.class,
                        () -> coinValidator.validateUpdateCoin(updateRequest)
                );

        assertEquals("Coin not found with id: 999", exception.getMessage());

        verify(coinRepository).existsById(999L);
        verify(coinRepository, never()).existsByCoingeckoId(anyString());
    }

    @Test
    void shouldThrowExceptionWhenUpdateCoinIdIsNull() {
        UpdateCoinRequestDto updateRequest = new UpdateCoinRequestDto();
        updateRequest.setId(null);
        updateRequest.setCoingeckoId("bitcoin");
        updateRequest.setSymbol("BTC");
        updateRequest.setName("Bitcoin");
        updateRequest.setImage("bitcoin.png");

        CoinNotFoundException exception =
                assertThrows(
                        CoinNotFoundException.class,
                        () -> coinValidator.validateUpdateCoin(updateRequest)
                );

        assertEquals("Coin not found with id: null", exception.getMessage());

        //verify(coinRepository).existsById(null);
        verify(coinRepository, never()).existsByCoingeckoId(anyString());
    }

    @Test
    void shouldThrowExceptionWhenUpdateCoingeckoIdAlreadyExists() {
        UpdateCoinRequestDto updateRequest = new UpdateCoinRequestDto();
        updateRequest.setId(1L);
        updateRequest.setCoingeckoId("ethereum");
        updateRequest.setSymbol("ETH2");
        updateRequest.setName("Ethereum Updated");
        updateRequest.setImage("ethereum2.png");

        when(coinRepository.existsById(1L)).thenReturn(true);
        when(coinRepository.existsByCoingeckoId("ethereum")).thenReturn(true);

        CoinAlreadyExistsException exception =
                assertThrows(
                        CoinAlreadyExistsException.class,
                        () -> coinValidator.validateUpdateCoin(updateRequest)
                );

        assertEquals("Coin already exists with CoinGecko ID, or symbol: ethereum", exception.getMessage());

        verify(coinRepository).existsById(1L);
        verify(coinRepository).existsByCoingeckoId("ethereum");
        verify(coinRepository, never()).existsBySymbolIgnoreCase(anyString());
    }

    @Test
    void shouldThrowExceptionWhenUpdateSymbolAlreadyExists() {
        UpdateCoinRequestDto updateRequest = new UpdateCoinRequestDto();
        updateRequest.setId(1L);
        updateRequest.setCoingeckoId("bitcoin-updated");
        updateRequest.setSymbol("BTC");
        updateRequest.setName("Bitcoin Updated");
        updateRequest.setImage("bitcoin-updated.png");

        when(coinRepository.existsById(1L)).thenReturn(true);
        when(coinRepository.existsByCoingeckoId("bitcoin-updated")).thenReturn(false);
        when(coinRepository.existsBySymbolIgnoreCase("BTC")).thenReturn(true);

        CoinAlreadyExistsException exception =
                assertThrows(
                        CoinAlreadyExistsException.class,
                        () -> coinValidator.validateUpdateCoin(updateRequest)
                );

        assertEquals("Coin already exists with CoinGecko ID, or symbol: BTC", exception.getMessage());

        verify(coinRepository).existsById(1L);
        verify(coinRepository).existsByCoingeckoId("bitcoin-updated");
        verify(coinRepository).existsBySymbolIgnoreCase("BTC");
    }
}