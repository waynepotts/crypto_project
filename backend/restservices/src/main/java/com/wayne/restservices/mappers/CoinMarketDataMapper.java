package com.wayne.restservices.mappers;


import com.wayne.restservices.dtos.CoinHistoryPagedResponseDto;
import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.CoinHistoryResponseDto;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoChartPointDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoMarketChartDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.wayne.restservices.utils.ChronoUnitConverter;

public class CoinMarketDataMapper {

    private static final Logger log = LoggerFactory.getLogger(CoinMarketDataMapper.class);
    public static CoinMarketData fromDto(@NonNull CoinGeckoCoinDto dto) {
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
        Instant createdAt = Instant.now();
        marketData.setGranularity(ChronoUnitConverter.getGranularity(createdAt));
        marketData.setSource("coingecko");
        marketData.setCreatedAt(createdAt);
        return marketData;
    }
    public static List<CoinMarketData> fromHistory(CoinGeckoMarketChartDto dto, Coin coin) {
        if(dto.getPrices() == null || dto.getPrices().isEmpty()) {
            // TODO: fix properly
            return new ArrayList<>();
        }
        List<CoinMarketData> entities = new ArrayList<>(dto.getPrices().size());
        for(int index = 0; index < dto.getPrices().size(); index++) {
            CoinMarketData marketData = new CoinMarketData();
            CoinGeckoChartPointDto pointDto = dto.getPrices().get(index);
            marketData.setCurrentPrice(pointDto.getValue());
            marketData.setMarketCap(dto.getMarketCaps().get(index).getValue());
            marketData.setTotalVolume(dto.getTotalVolumes().get(index).getValue());
            marketData.setLastUpdated(pointDto.getTimeStamp());
            marketData.setCoin(coin);
            marketData.setGranularity(ChronoUnitConverter.getGranularity(pointDto.getTimeStamp()));
            marketData.setSource("coingecko");
            entities.add(marketData);
        }
        return entities;
    }

    public static CoinHistoryPointDto toDto(CoinMarketData marketData) {
        return new CoinHistoryPointDto(
                marketData.getLastUpdated(),
                marketData.getCurrentPrice(),
                marketData.getMarketCap(),
                marketData.getTotalVolume()
        );
    }
    public static CoinHistoryResponseDto fromPaged(@NonNull CoinHistoryPagedResponseDto dto, double expected){
        long elements = dto.getTotalElements();
        return new CoinHistoryResponseDto(dto.getContent(), elements / expected, null);
    }

    public static CoinHistoryResponseDto fromCoinGecko(@NonNull CoinGeckoMarketChartDto dto) {
        List<CoinHistoryPointDto> chartData = new ArrayList<>();
        List<CoinGeckoChartPointDto> priceList = dto.getPrices();
        for (int i = 0; i < priceList.size(); i++) {
            Instant key = priceList.get(i).getTimeStamp();
            CoinHistoryPointDto pointDto = new CoinHistoryPointDto(
                    key,
                    priceList.get(i).getValue(),
                    dto.getMarketCaps().get(i).getValue(),
                    dto.getTotalVolumes().get(i).getValue()
            );
            chartData.add(pointDto);
        }
        return new CoinHistoryResponseDto(chartData, 1.0D, null);
    }
    public static CoinMarketDataDto toMarketDataDto(CoinMarketData data){
        return new CoinMarketDataDto(
                data.getId(),
                data.getCoin().getId(),
                data.getCoin().getName(),
                data.getCoin().getSymbol(),
                data.getCurrentPrice(),
                data.getMarketCap(),
                data.getMarketCapRank(),
                data.getLastUpdated(),
                data.getPriceChange24h()
        );
    }

    private static final BigDecimal MAX = new BigDecimal("99999999999999999999");
    private static BigDecimal toFinancialScale(BigDecimal value) {
        if(value != null) {
            value = value.min(MAX);
        }

        return value != null ?value.setScale(18, RoundingMode.HALF_UP): null;
    }
}
