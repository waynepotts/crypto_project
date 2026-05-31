package com.wayne.restservices.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoinNotFoundExceptionTest {

    @Test
    void shouldContainIdInMessage() {
        CoinNotFoundException ex = new CoinNotFoundException(42L);

        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("42"));
        assertTrue(ex.getMessage().contains("Coin not found"));
    }

    @Test
    void shouldContainCorrectPrefixInMessage() {
        CoinNotFoundException ex = new CoinNotFoundException(1L);

        assertTrue(ex.getMessage().startsWith("Coin not found with id: "));
    }

    @Test
    void shouldHaveRightExceptionType() {
        CoinNotFoundException ex = new CoinNotFoundException(99L);

        assertTrue(ex instanceof RuntimeException);
        assertTrue(ex instanceof CoinNotFoundException);
    }

    @Test
    void shouldHandleZeroIdInMessage() {
        CoinNotFoundException ex = new CoinNotFoundException(0L);

        assertTrue(ex.getMessage().contains("Coin not found with id: 0"));
    }
}
