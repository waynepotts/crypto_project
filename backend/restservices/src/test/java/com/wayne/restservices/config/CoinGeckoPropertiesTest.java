package com.wayne.restservices.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoinGeckoPropertiesTest {

    private CoinGeckoProperties properties;

    @BeforeEach
    void setUp() {
        properties = new CoinGeckoProperties();
    }

    @Test
    void shouldSetAndReturnKey() {
        properties.setKey("test-api-key");
        assertEquals("test-api-key", properties.getKey());
    }

    @Test
    void shouldSetAndReturnUrl() {
        properties.setUrl("https://api.coingecko.com");
        assertEquals("https://api.coingecko.com", properties.getUrl());
    }

    @Test
    void shouldAllowNullValues() {
        assertNull(properties.getKey());
        assertNull(properties.getUrl());
    }

    @Test
    void shouldUpdateKeyMultipleTimes() {
        properties.setKey("key-1");
        assertEquals("key-1", properties.getKey());
        properties.setKey("key-2");
        assertEquals("key-2", properties.getKey());
    }

    @Test
    void shouldUpdateUrlMultipleTimes() {
        properties.setUrl("https://url-1.com");
        assertEquals("https://url-1.com", properties.getUrl());
        properties.setUrl("https://url-2.com");
        assertEquals("https://url-2.com", properties.getUrl());
    }
}
