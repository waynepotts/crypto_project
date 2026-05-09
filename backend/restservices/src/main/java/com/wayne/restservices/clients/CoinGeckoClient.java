package com.wayne.restservices.clients;

import com.wayne.restservices.config.CoinGeckoProperties;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class CoinGeckoClient {

    private final RestClient restClient;

    private final CoinGeckoProperties properties;

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";
    public CoinGeckoClient(
            CoinGeckoProperties properties) {
        this.properties = properties;

        this.restClient = RestClient.create(BASE_URL);
                /*.url("https://api.coingecko.com/api/v3")
                .build();*/
    }

    public List<CoinGeckoCoinDto> getMarkets() {

        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/coins/markets")
                                .queryParam("vs_currency", "usd")
                                .queryParam("per_page", 100)
                                .queryParam("page", 1)
                                .queryParam("x_cg_demo_api_key", properties.getKey())
                                .build()
                )
                .retrieve()
                .body(
                        new ParameterizedTypeReference<>() {}
                );
    }
}