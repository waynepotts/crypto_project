package com.wayne.restservices.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoinAlreadyExistsExceptionTest {

    @Test
    void shouldContainCoinIdInMessage() {
        CoinAlreadyExistsException ex = new CoinAlreadyExistsException("bitcoin");

        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("bitcoin"));
        assertTrue(ex.getMessage().contains("Coin already exists"));
    }

    @Test
    void shouldContainCorrectPrefixInMessage() {
        CoinAlreadyExistsException ex = new CoinAlreadyExistsException("ethereum");

        assertTrue(ex.getMessage().startsWith("Coin already exists with CoinGecko ID: "));
    }

    @Test
    void shouldHaveRightExceptionType() {
        CoinAlreadyExistsException ex = new CoinAlreadyExistsException("test");

        assertTrue(ex instanceof RuntimeException);
        assertTrue(ex instanceof CoinAlreadyExistsException);
    }

    @Test
    void shouldHandleEmptyCoinIdInMessage() {
        CoinAlreadyExistsException ex = new CoinAlreadyExistsException("");

        assertTrue(ex.getMessage().contains("CoinGecko ID: "));
    }
}
