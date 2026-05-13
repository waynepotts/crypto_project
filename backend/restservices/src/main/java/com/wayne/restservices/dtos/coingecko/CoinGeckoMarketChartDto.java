package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinGeckoMarketChartDto {
    @JsonProperty("prices")
    private List<CoinGeckoChartPointDto> prices;
    @JsonProperty("market_caps")
    private List<CoinGeckoChartPointDto> marketCaps;
    @JsonProperty("total_volumes")
    private List<CoinGeckoChartPointDto> totalVolumes;

    public List<CoinGeckoChartPointDto> getPrices() {
        return prices;
    }

    public void setPrices(List<CoinGeckoChartPointDto> prices) {
        this.prices = prices;
    }

    public List<CoinGeckoChartPointDto> getMarketCaps() {
        return marketCaps;
    }

    public void setMarketCaps(List<CoinGeckoChartPointDto> marketCaps) {
        this.marketCaps = marketCaps;
    }

    public List<CoinGeckoChartPointDto> getTotalVolumes() {
        return totalVolumes;
    }

    public void setTotalVolumes(List<CoinGeckoChartPointDto> totalVolumes) {
        this.totalVolumes = totalVolumes;
    }
}
