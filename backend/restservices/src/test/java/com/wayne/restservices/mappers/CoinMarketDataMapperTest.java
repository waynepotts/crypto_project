package com.wayne.restservices.mappers;

import com.wayne.restservices.dtos.*;
import com.wayne.restservices.dtos.coingecko.CoinGeckoChartPointDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import com.wayne.restservices.dtos.coingecko.CoinGeckoMarketChartDto;
import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoinMarketDataMapperTest {

    @Test
    void shouldMapCoinGeckoDtoToMarketData() {
        Instant now = Instant.now();
        CoinGeckoCoinDto dto = createCoinGeckoDto(now);

        CoinMarketData marketData = CoinMarketDataMapper.fromDto(dto);

        assertNotNull(marketData);
        assertEquals(0, BigDecimal.valueOf(50000).compareTo(marketData.getCurrentPrice()));
        assertEquals(0, BigDecimal.valueOf(1000000000L).compareTo(marketData.getMarketCap()));
        assertEquals(1, marketData.getMarketCapRank());
        assertEquals(0, BigDecimal.valueOf(50000000L).compareTo(marketData.getTotalVolume()));
        assertEquals(0, BigDecimal.valueOf(52000).compareTo(marketData.getHigh24h()));
        assertEquals(0, BigDecimal.valueOf(48000).compareTo(marketData.getLow24h()));
        assertEquals(0, BigDecimal.valueOf(100).compareTo(marketData.getPriceChange24h()));
        assertEquals(0, BigDecimal.valueOf(2.5).compareTo(marketData.getPriceChangePercentage24h()));
        assertEquals(0, BigDecimal.valueOf(3.0).compareTo(marketData.getMarketCapChangePercentage24h()));
        assertEquals(0, BigDecimal.valueOf(5000000L).compareTo(marketData.getMarketCapChange24h()));
        assertEquals(0, BigDecimal.valueOf(18000000).compareTo(marketData.getCirculatingSupply()));
        assertEquals(0, BigDecimal.valueOf(21000000).compareTo(marketData.getTotalSupply()));
        assertEquals(0, BigDecimal.valueOf(21000000).compareTo(marketData.getMaxSupply()));
        assertEquals(0, BigDecimal.valueOf(69000).compareTo(marketData.getAth()));
        assertEquals(0, BigDecimal.valueOf(-10.5).compareTo(marketData.getAthChangePercentage()));
        assertEquals(0, BigDecimal.valueOf(3000).compareTo(marketData.getAtl()));
        assertEquals(0, BigDecimal.valueOf(50.0).compareTo(marketData.getAtlChangePercentage()));
        assertEquals(now, marketData.getLastUpdated());
        assertEquals(now, marketData.getAthDate());
        assertEquals(now, marketData.getAtlDate());
        assertEquals(1, marketData.getMarketCapRankWithRehypothecated());
        assertEquals(0, BigDecimal.valueOf(1000000000000L).compareTo(marketData.getFullyDilutedValuation()));
        assertEquals("coingecko", marketData.getSource());
        assertNotNull(marketData.getCreatedAt());
        assertNotNull(marketData.getGranularity());
    }

    @Test
    void shouldMapCoinMarketDataToHistoryPointDto() {
        Instant now = Instant.now();
        CoinMarketData marketData = new CoinMarketData();
        marketData.setCurrentPrice(BigDecimal.valueOf(50000));
        marketData.setMarketCap(BigDecimal.valueOf(1000000000L));
        marketData.setTotalVolume(BigDecimal.valueOf(50000000L));
        marketData.setLastUpdated(now);

        CoinHistoryPointDto dto = CoinMarketDataMapper.toDto(marketData);

        assertNotNull(dto);
        assertEquals(BigDecimal.valueOf(50000), dto.price());
        assertEquals(BigDecimal.valueOf(1000000000L), dto.marketCap());
        assertEquals(BigDecimal.valueOf(50000000L), dto.volume());
        assertEquals(now, dto.timestamp());
    }

    @Test
    void shouldMapPagedResponseToHistoryResponse() {
        CoinHistoryPointDto point = new CoinHistoryPointDto(Instant.now(), BigDecimal.valueOf(50000), null, null);

        Page<CoinHistoryPointDto> page = new PageImpl<>(
                List.of(point), PageRequest.of(0, 20), 1);

        CoinHistoryPagedResponseDto pagedDto = new CoinHistoryPagedResponseDto(page);

        CoinResponseDto coinDto = new CoinResponseDto(1L,"bitcoin", "btc","", "", new ArrayList<>());
        CoinHistoryResponseDto response = CoinMarketDataMapper.fromPaged(pagedDto, coinDto, 24.0);

        assertNotNull(response);
        assertEquals(1, response.chartData().size());
        assertEquals(BigDecimal.valueOf(50000), response.chartData().get(0).price());
        assertEquals(0.041666666666666664, response.completeness(), 0.0001);
    }

    @Test
    void shouldMapCoinGeckoMarketChartToHistoryResponse() {
        Instant now = Instant.ofEpochMilli(Instant.now().toEpochMilli());
        CoinGeckoChartPointDto pricePoint = new CoinGeckoChartPointDto(
                List.of(now.toEpochMilli(), 50000));
        CoinGeckoChartPointDto volumePoint = new CoinGeckoChartPointDto(
                List.of(now.toEpochMilli(), 1000000));
        CoinGeckoChartPointDto marketCapPoint = new CoinGeckoChartPointDto(
                List.of(now.toEpochMilli(), 2000000000L));

        CoinGeckoMarketChartDto chartDto = new CoinGeckoMarketChartDto();
        chartDto.setPrices(List.of(pricePoint));
        chartDto.setTotalVolumes(List.of(volumePoint));
        chartDto.setMarketCaps(List.of(marketCapPoint));

        CoinHistoryResponseDto response = CoinMarketDataMapper.fromCoinGecko(chartDto);

        assertNotNull(response);
        assertEquals(1, response.chartData().size());
        assertEquals(1.0, response.completeness(), 0.001);

        CoinHistoryPointDto point = response.chartData().get(0);
        assertEquals(0, BigDecimal.valueOf(50000).compareTo(point.price()));
        assertEquals(0, BigDecimal.valueOf(1000000).compareTo(point.volume()));
        assertEquals(0, BigDecimal.valueOf(2000000000L).compareTo(point.marketCap()));
        assertEquals(now, point.timestamp());
    }

    @Test
    void shouldMapCoinMarketDataToMarketDataDto() {
        Coin coin = new Coin();
        coin.setId(1L);
        coin.setName("Bitcoin");
        coin.setSymbol("BTC");

        Instant now = Instant.now();
        CoinMarketData marketData = new CoinMarketData();
        marketData.setId(10L);
        marketData.setCoin(coin);
        marketData.setCurrentPrice(BigDecimal.valueOf(50000));
        marketData.setMarketCap(BigDecimal.valueOf(1000000000L));
        marketData.setMarketCapRank(1);
        marketData.setLastUpdated(now);
        marketData.setPriceChange24h(BigDecimal.valueOf(100));

        CoinMarketDataDto dto = CoinMarketDataMapper.toMarketDataDto(marketData);

        assertNotNull(dto);
        assertEquals(10L, dto.id());
        assertEquals(1L, dto.coinId());
        assertEquals("Bitcoin", dto.name());
        assertEquals("BTC", dto.symbol());
        assertEquals(BigDecimal.valueOf(50000), dto.currentPrice());
        assertEquals(BigDecimal.valueOf(1000000000L), dto.marketCap());
        assertEquals(1, dto.marketCapRank());
        assertEquals(now, dto.lastUpdated());
        assertEquals(BigDecimal.valueOf(100), dto.priceChange24h());
    }

    @Test
    void shouldCapAndScaleFinancialValues() {
        CoinGeckoCoinDto dto = createCoinGeckoDto(Instant.now());
        dto.setPriceChange24h(new BigDecimal("99999999999999999999"));

        CoinMarketData marketData = CoinMarketDataMapper.fromDto(dto);

        BigDecimal max = new BigDecimal("99999999999999999999");
        assertEquals(max, marketData.getPriceChange24h().setScale(0, BigDecimal.ROUND_HALF_UP));
        assertEquals(18, marketData.getPriceChange24h().scale());
    }

    private CoinGeckoCoinDto createCoinGeckoDto(Instant now) {
        CoinGeckoCoinDto dto = new CoinGeckoCoinDto();
        dto.setId("bitcoin");
        dto.setSymbol("BTC");
        dto.setName("Bitcoin");
        dto.setImage("bitcoin.png");
        dto.setCurrentPrice(BigDecimal.valueOf(50000));
        dto.setMarketCap(BigDecimal.valueOf(1000000000L));
        dto.setMarketCapRank(1);
        dto.setFullyDilutedValuation(BigDecimal.valueOf(1000000000000L));
        dto.setTotalVolume(BigDecimal.valueOf(50000000L));
        dto.setHigh24h(BigDecimal.valueOf(52000));
        dto.setLow24h(BigDecimal.valueOf(48000));
        dto.setPriceChange24h(BigDecimal.valueOf(100));
        dto.setPriceChangePercentage24h(BigDecimal.valueOf(2.5));
        dto.setMarketCapChange24h(BigDecimal.valueOf(5000000L));
        dto.setMarketCapChangePercentage24h(BigDecimal.valueOf(3.0));
        dto.setCirculatingSupply(BigDecimal.valueOf(18000000));
        dto.setTotalSupply(BigDecimal.valueOf(21000000));
        dto.setMaxSupply(BigDecimal.valueOf(21000000));
        dto.setAth(BigDecimal.valueOf(69000));
        dto.setAthChangePercentage(BigDecimal.valueOf(-10.5));
        dto.setAthDate(now);
        dto.setAtl(BigDecimal.valueOf(3000));
        dto.setAtlChangePercentage(BigDecimal.valueOf(50.0));
        dto.setAtlDate(now);
        dto.setLastUpdated(now);
        dto.setMarketCapRankWithRehypothecated(1);
        return dto;
    }
}
