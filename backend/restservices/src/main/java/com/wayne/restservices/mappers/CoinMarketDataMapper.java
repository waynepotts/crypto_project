package com.wayne.restservices.mappers;


import com.wayne.restservices.dtos.*;
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
        final Instant granular = ChronoUnitConverter.normalizeFiveMinutes(dto.getLastUpdated());
        marketData.setGranularTimestamp(granular);
        marketData.setGranularity(ChronoUnitConverter.getGranularity(granular));
        marketData.setSource("coingecko");
        marketData.setCreatedAt(Instant.now());
        return marketData;
    }
    public static List<CoinMarketData> fromHistory(CoinGeckoMarketChartDto dto, Coin coin) {

        List<CoinMarketData> entities = null;
        try {
            entities = new ArrayList<>(dto.getPrices().size());
            for (int index = 0; index < dto.getPrices().size(); index++) {
                CoinMarketData marketData = new CoinMarketData();
                CoinGeckoChartPointDto pointDto = dto.getPrices().get(index);
                marketData.setCurrentPrice(pointDto.getValue());
                marketData.setMarketCap(dto.getMarketCaps().get(index).getValue());
                marketData.setTotalVolume(dto.getTotalVolumes().get(index).getValue());
                marketData.setLastUpdated(pointDto.getTimeStamp());
                final Instant granular = ChronoUnitConverter.normalizeFiveMinutes(pointDto.getTimeStamp());
                marketData.setCreatedAt(Instant.now());
                marketData.setGranularTimestamp(granular);
                marketData.setCoin(coin);
                marketData.setGranularity(ChronoUnitConverter.getGranularity(granular));
                marketData.setSource("coingecko");
                entities.add(marketData);
            }
        } catch(NullPointerException npe){
            entities = new ArrayList<>();
            log.info("handled NPE in CoinMarketDataMapper::fromHistory, returning empty list. " + npe.getMessage());
        }
        return entities;
    }

    public static CoinHistoryPointDto toDto(CoinMarketData marketData) {
        return new CoinHistoryPointDto(
                ChronoUnitConverter.normalizeFiveMinutes(marketData.getLastUpdated()),
                marketData.getCurrentPrice(),
                marketData.getMarketCap(),
                marketData.getTotalVolume()
        );
    }
    public static CoinHistoryResponseDto fromPaged(@NonNull CoinHistoryPagedResponseDto dto, CoinResponseDto coinDto, Instant from, Instant to, ChronoUnit granularity) {
        double elements = dto.getTotalElements();
        long seconds = switch (granularity) {
            case MINUTES -> from.until(to, ChronoUnit.MINUTES) / 5;
            case HOURS -> from.until(to, ChronoUnit.HOURS);
            case DAYS -> from.until(to, ChronoUnit.DAYS);
            default -> 1;
        };
        log.info("elements {} {} {}", elements, seconds, granularity);
        return new CoinHistoryResponseDto(dto.getContent(),elements / seconds , coinDto);
    }
    public static CoinHistoryResponseDto fromList(@NonNull List<CoinHistoryPointDto> list, CoinResponseDto coinDto, Instant from, Instant to, ChronoUnit granularity) {
        double elements = list.size();
        long seconds = switch (granularity) {
            case MINUTES -> from.until(to, ChronoUnit.MINUTES) / 5;
            case HOURS -> from.until(to, ChronoUnit.HOURS);
            case DAYS -> from.until(to, ChronoUnit.DAYS);
            default -> 1;
        };
        return new CoinHistoryResponseDto(list,elements / seconds , coinDto);
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
