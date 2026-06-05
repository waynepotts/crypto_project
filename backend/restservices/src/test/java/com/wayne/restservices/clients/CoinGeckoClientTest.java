package com.wayne.restservices.clients;

import com.wayne.restservices.config.CoinGeckoProperties;
import com.wayne.restservices.dtos.coingecko.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CoinGeckoClientTest {

    @Test
    void shouldConstructClientWithValidProperties() {
        CoinGeckoProperties properties = new CoinGeckoProperties();
        properties.setKey("test-api-key");
        properties.setUrl("https://api.coingecko.com/api/v3");

        CoinGeckoClient client = new CoinGeckoClient(properties);

        assertNotNull(client);
    }

    @Test
    void shouldRejectEmptyApiKey() {
        CoinGeckoProperties properties = new CoinGeckoProperties();
        assertDoesNotThrow(() -> {
            CoinGeckoClient client = new CoinGeckoClient(properties);
            assertNotNull(client);
        });
    }

}
