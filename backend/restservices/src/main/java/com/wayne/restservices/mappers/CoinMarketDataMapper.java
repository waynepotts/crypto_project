package com.wayne.restservices.mappers;


import com.wayne.restservices.dtos.CoinHistoryPagedResponseDto;
import com.wayne.restservices.dtos.CoinHistoryPointDto;
import com.wayne.restservices.dtos.CoinHistoryResponseDto;
import com.wayne.restservices.dtos.CoinMarketDataDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoChartPointDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoMarketChartDto;
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
        Instant bucket = ChronoUnitConverter.normalizeFiveMinutes(createdAt);
        ChronoUnit unit = ChronoUnit.MINUTES;
        Instant bucketStart = bucket.minus(299, ChronoUnit.SECONDS);
        Instant hourly = ChronoUnitConverter.normalizeHourly(createdAt);
        if(bucketStart.isBefore(hourly) && hourly.plus(299, ChronoUnit.SECONDS).isAfter(bucket)) {
            unit = ChronoUnit.HOURS;
            Instant daily = ChronoUnitConverter.normalizeDaily(createdAt);
            if(bucketStart.isBefore(daily) && daily.plus(299, ChronoUnit.SECONDS).isAfter(bucket)) {
                unit = ChronoUnit.DAYS;
            }
        }
        marketData.setGranularity(unit);
        marketData.setSource("coingecko");
        marketData.setCreatedAt(createdAt);
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
    public static CoinHistoryResponseDto fromPaged(@NonNull CoinHistoryPagedResponseDto dto, double expected){
        CoinHistoryResponseDto response = new CoinHistoryResponseDto();
        response.setChartData(dto.getContent());
        long elements = dto.getTotalElements();
        response.setCompleteness(elements / expected);
        return response;
    }

    public static CoinHistoryResponseDto fromCoinGecko(@NonNull CoinGeckoMarketChartDto dto) {
        CoinHistoryResponseDto dtoResponse = new CoinHistoryResponseDto();
        List<CoinHistoryPointDto> chartData = new ArrayList<>();
        List<CoinGeckoChartPointDto> priceList = dto.getPrices();
        for (int i = 0; i < priceList.size(); i++) {
            Instant key = priceList.get(i).getTimeStamp();
            CoinHistoryPointDto pointDto = new CoinHistoryPointDto();
            pointDto.setPrice(priceList.get(i).getValue());
            pointDto.setTimestamp(key);
            pointDto.setVolume(dto.getTotalVolumes().get(i).getValue());
            pointDto.setMarketCap(dto.getMarketCaps().get(i).getValue());
            chartData.add(pointDto);
        }
        dtoResponse.setCompleteness(1.0D);
        dtoResponse.setChartData(chartData);
        return dtoResponse;
    }
    public static CoinMarketDataDto toMarketDataDto(CoinMarketData data){
        CoinMarketDataDto dto = new CoinMarketDataDto();
        dto.setId(data.getId());
        dto.setCoinId(data.getCoin().getId());
        dto.setName(data.getCoin().getName());
        dto.setSymbol(data.getCoin().getSymbol());
        dto.setMarketCap(data.getMarketCap());
        dto.setCurrentPrice(data.getCurrentPrice());
        dto.setLastUpdated(data.getLastUpdated());
        dto.setMarketCapRank(data.getMarketCapRank());
        dto.setPriceChange24h(data.getPriceChange24h());
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
