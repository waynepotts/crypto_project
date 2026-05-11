package com.wayne.restservices.mappers;

import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.entities.jpa.CoinMarketData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class CoinMarketDataMapper {

    public static CoinMarketData fromDto(CoinGeckoCoinDto dto) {
        CoinMarketData marketData = new CoinMarketData();
        marketData.setAth(dto.getAth());
        marketData.setAtl(dto.getAtl());
        marketData.setLastUpdated(dto.getLastUpdated());
        marketData.setMarketCap(dto.getMarketCap());
        marketData.setCurrentPrice(dto.getCurrentPrice());
        marketData.setAtlChangePercentage(toFinancialScale(dto.getAtlChangePercentage()));
        marketData.setAthChangePercentage(toFinancialScale(dto.getAthChangePercentage()));
        marketData.setAtlDate(dto.getAtlDate());
        marketData.setAthDate(dto.getAthDate());
        marketData.setCirculatingSupply(toFinancialScale(dto.getCirculatingSupply()));
        marketData.setFullyDilutedValuation(dto.getFullyDilutedValuation());
        marketData.setHigh24h(toFinancialScale(dto.getHigh24h()));
        marketData.setLow24h(toFinancialScale(dto.getLow24h()));
        marketData.setMarketCapChange24h(dto.getMarketCapChange24h());
        marketData.setMarketCapRank(dto.getMarketCapRank());
        marketData.setMarketCapChangePercentage24h(toFinancialScale(dto.getMarketCapChangePercentage24h()));
        marketData.setMarketCapRankWithRehypothecated(dto.getMarketCapRankWithRehypothecated());
        marketData.setMaxSupply(toFinancialScale(dto.getMaxSupply()));
        marketData.setTotalSupply(toFinancialScale(dto.getTotalSupply()));
        marketData.setTotalVolume(dto.getTotalVolume());
        marketData.setPriceChangePercentage24h(toFinancialScale(dto.getPriceChangePercentage24h()));
        marketData.setPriceChange24h(toFinancialScale(dto.getPriceChange24h()));
        marketData.setSource("coingecko");
        marketData.setCreatedAt(Instant.now());
        return marketData;
    }
    public static CoinHistoryPointDto toDto(CoinMarketData marketData) {
        CoinHistoryPointDto dto = new CoinHistoryPointDto();
        dto.setPrice(marketData.getCurrentPrice());
        dto.setMarketCap(marketData.getMarketCap());
        dto.setVolume(marketData.getTotalVolume());
        dto.setTimestamp(marketData.getLastUpdated());
        return  dto;
    }

    private static final BigDecimal MAX = new BigDecimal("99999999999999999999");
    private static BigDecimal toFinancialScale(BigDecimal value) {
        if(value != null) {
            value = value.min(MAX);
        }

        return value != null ?value.setScale(18, RoundingMode.HALF_UP): null;
    }
}
