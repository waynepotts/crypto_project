package com.wayne.restservices.controllers;

import com.wayne.restservices.dtos.*;
import com.wayne.restservices.exceptions.CoinNotFoundException;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.services.CoinMarketDataService;
import com.wayne.restservices.services.CoinService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test","dev"})
class CoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoinService service;

    @MockitoBean
    private CoinMarketDataService marketDataService;

    @MockitoBean
    private CoinRepository repository;

    @Test
    void shouldReturnCoins() throws Exception {
        CoinResponseDto dto = new CoinResponseDto(null, "coingeckoId_btc", "BTC", "Bitcoin", null, new ArrayList<>());
        when(service.getAllCoins())
                .thenReturn(List.of(dto));
        mockMvc.perform(get("/api/v1/coins/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Bitcoin"));
    }

    @Test
    void shouldReturnCoin() throws Exception {
        CoinResponseDto dto = new CoinResponseDto(null, null, null, "Bitcoin", null, new ArrayList<>());

        when(service.getCoin(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/coins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Bitcoin"));
    }

    @Test
    void shouldReturnCoinNotFound() throws Exception {
        when(service.getCoin(999L)).thenThrow(new CoinNotFoundException(999L));
        mockMvc.perform(get("/api/v1/coins/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Coin not found with id: 999"));
    }

    @Test
    void shouldReturnPagedCoins() throws Exception {
        CoinResponseDto dto = new CoinResponseDto(1L, null, "BTC", "Bitcoin", null, new ArrayList<>());

        PagedResponseDto<CoinResponseDto> paged = new PagedResponseDto<>(
                new PageImpl<>(List.of(dto), PageRequest.of(0, 20), 1));

        when(service.getCoins(any(Pageable.class))).thenReturn(paged);

        mockMvc.perform(get("/api/v1/coins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Bitcoin"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.page").value(0));
    }

    @Test
    void shouldSearchCoins() throws Exception {
        CoinResponseDto dto = new CoinResponseDto(1L, null, "BTC", "Bitcoin", null, new ArrayList<>());

        PagedResponseDto<CoinResponseDto> paged = new PagedResponseDto<>(
                new PageImpl<>(List.of(dto), PageRequest.of(0, 20), 1));

        when(service.searchCoins(eq("bit"), any(Pageable.class))).thenReturn(paged);

        mockMvc.perform(get("/api/v1/coins/search?q=bit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Bitcoin"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldReturnEmptySearchResults() throws Exception {
        PagedResponseDto<CoinResponseDto> paged = new PagedResponseDto<>(
                new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));

        when(service.searchCoins(eq("xyz"), any(Pageable.class))).thenReturn(paged);

        mockMvc.perform(get("/api/v1/coins/search?q=xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void shouldReturnMarketCapRank() throws Exception {
        CoinMarketDataDto dto = new CoinMarketDataDto(null, 1L, "Bitcoin", "BTC", BigDecimal.valueOf(50000), null, 1, null, null);

        when(marketDataService.GetMarketDataByMarketCapRankRange(0, 5))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/coins/marketcaprank?start=0&end=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bitcoin"))
                .andExpect(jsonPath("$[0].marketCapRank").value(1));
    }
}
