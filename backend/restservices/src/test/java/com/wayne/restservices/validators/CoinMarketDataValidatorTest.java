package com.wayne.restservices.validators;

import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CoinMarketDataValidatorTest {

    private final CoinMarketDataValidator validator = new CoinMarketDataValidator();
    private CoinGeckoCoinDto dto;

    @BeforeEach
    void setUp() {
        dto = new CoinGeckoCoinDto();
        dto.setId("bitcoin");
        dto.setCurrentPrice(BigDecimal.valueOf(50000));
        dto.setMarketCap(BigDecimal.valueOf(1000000000));
    }

    @Test
    void shouldPassValidationWithValidDto() {
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        dto.setId(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertEquals("id cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCurrentPriceIsNull() {
        dto.setCurrentPrice(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertEquals("current_price cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPriceChangePercentageIsTooLarge() {
        dto.setPriceChangePercentage24h(BigDecimal.valueOf(2000000));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertTrue(ex.getMessage().contains("price_change_percentage_24h"));
    }

    @Test
    void shouldThrowWhenMarketCapChangePercentageIsTooLarge() {
        dto.setMarketCapChangePercentage24h(BigDecimal.valueOf(2000000));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertTrue(ex.getMessage().contains("market_cap_change_percentage_24h"));
    }

    @Test
    void shouldThrowWhenAthChangePercentageIsTooLarge() {
        dto.setAthChangePercentage(BigDecimal.valueOf(2000000));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertTrue(ex.getMessage().contains("ath_change_percentage"));
    }

    @Test
    void shouldThrowWhenAtlChangePercentageIsTooLarge() {
        dto.setAtlChangePercentage(BigDecimal.valueOf(2000000));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertTrue(ex.getMessage().contains("atl_change_percentage"));
    }

    @Test
    void shouldThrowWhenMarketCapIsNegative() {
        dto.setMarketCap(BigDecimal.valueOf(-100));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(dto));
        assertTrue(ex.getMessage().contains("market_cap"));
    }

    @Test
    void shouldPassWhenPercentageIsNull() {
        dto.setPriceChangePercentage24h(null);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldPassWhenMarketCapIsNull() {
        dto.setMarketCap(null);
        assertDoesNotThrow(() -> validator.validate(dto));
    }
}
