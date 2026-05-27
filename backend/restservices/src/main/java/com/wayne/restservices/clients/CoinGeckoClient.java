package com.wayne.restservices.clients;

import com.wayne.restservices.config.CoinGeckoProperties;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;

import com.wayne.restservices.dtos.coingecko.CoinGeckoExchangeResponseDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoMarketChartDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class CoinGeckoClient {

    private final RestClient restClient;

    private final CoinGeckoProperties properties;

    public CoinGeckoClient(
            CoinGeckoProperties properties) {
        this.properties = properties;

        this.restClient = RestClient.create(properties.getUrl());
    }

    public List<CoinGeckoCoinDto> getMarkets(int page, int pageSize) {

        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/coins/markets")
                                .queryParam("vs_currency", "usd")
                                .queryParam("per_page", pageSize)
                                .queryParam("page", page)
                                .queryParam("x_cg_demo_api_key", properties.getKey())
                                .build()
                )
                .retrieve()
                .body(
                        new ParameterizedTypeReference<>() {}
                );
    }

    public enum Interval {
        mintutes5,
        hourly,
        daily
    }
    public CoinGeckoMarketChartDto  getCoinMarketChart(String coinGeckoId, Integer days, Interval interval) {
        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/coins/{id}/market_chart")
                                .queryParam("vs_currency", "usd")
                                .queryParam("days", days)
                                .queryParam("interval", interval.name().toLowerCase())
                                .build(coinGeckoId)
                )
                .header("x-cg-demo-api-key", properties.getKey())
                .retrieve()
                .body(CoinGeckoMarketChartDto.class);
    }

    public CoinGeckoExchangeResponseDto getExchangeRates() {
        return restClient.get()
                        .uri(uriBuilder ->
                                uriBuilder
                                        .path("/exchange_rates")
                                        .queryParam(
                                                "x_cg_demo_api_key",
                                                properties.getKey()
                                        )
                                        .build()
                        )
                        .retrieve()
                        .body(CoinGeckoExchangeResponseDto.class);
    }
}