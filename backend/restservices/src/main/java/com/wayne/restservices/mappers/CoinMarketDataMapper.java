package com.wayne.restservices.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.entities.jpa.CoinMarketData;

public class CoinMarketDataMapper {

    public static CoinMarketData fromDto(CoinGeckoCoinDto dto) {
        CoinMarketData marketData = new CoinMarketData();
        marketData.setAth(dto.getAth());
        marketData.setAtl(dto.getAtl());
        marketData.setLastUpdated(dto.getLastUpdated());
        marketData.setMarketCap(dto.getMarketCap());
        marketData.setCurrentPrice(dto.getCurrentPrice());
        marketData.setAtlChangePercentage(dto.getAtlChangePercentage());
        marketData.setAthChangePercentage(dto.getAthChangePercentage());
        marketData.setAtlDate(dto.getAtlDate());
        marketData.setAthDate(dto.getAthDate());
        marketData.setCirculatingSupply(dto.getCirculatingSupply());
        marketData.setFullyDilutedValuation(dto.getFullyDilutedValuation());
        marketData.setHigh24h(dto.getHigh24h());
        marketData.setLow24h(dto.getLow24h());
        marketData.setMarketCapChange24h(dto.getMarketCapChange24h());
        marketData.setMarketCapRank(dto.getMarketCapRank());
        marketData.setMarketCapChangePercentage24h(dto.getMarketCapChangePercentage24h());
        marketData.setMarketCapRankWithRehypothecated(dto.getMarketCapRankWithRehypothecated());
        marketData.setMaxSupply(dto.getMaxSupply());
        marketData.setTotalSupply(dto.getTotalSupply());
        marketData.setTotalVolume(dto.getTotalVolume());
        marketData.setPriceChangePercentage24h(dto.getPriceChangePercentage24h());
        marketData.setPriceChange24h(dto.getPriceChange24h());
        return marketData;
    }
}
