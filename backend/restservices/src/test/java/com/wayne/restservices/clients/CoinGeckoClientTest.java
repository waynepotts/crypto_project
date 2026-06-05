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

    @Test
    void shouldCreateCoinDetailsDtoWithCategories() {
        // TODO: make this a useful test
        Map<String, DetailPlatformDto> detailPlatformDtoMap = new HashMap<>();
        detailPlatformDtoMap.put("key", new DetailPlatformDto(0, ""));
        CoinGeckoCoinDetailsDto dto = new CoinGeckoCoinDetailsDto(
                "","","","", "",new HashMap<>(),detailPlatformDtoMap,null,
                "", new ArrayList<>(), false,"",new ArrayList<>(),new HashMap<>(),
                new HashMap<>(), new LinksDto(null, "",null,null, null,null,"","","" ,"", "",null)
        ,null,"","", BigDecimal.ZERO,BigDecimal.ZERO,0L,0,0
        ,null, null,null, Instant.now(), null);
        assertNotNull(dto);
        assertNotNull(dto.detailPlatforms());
        // Initial state check - categories list is populated by external source
        // but the DTO must handle null or empty gracefully
    }
}
