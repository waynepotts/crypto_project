package com.wayne.restservices.controllers;

import com.wayne.restservices.entities.dto.CoinResponseDto;
import com.wayne.restservices.repositories.CoinRepository;
import com.wayne.restservices.services.CoinService;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(CoinController.class)
@SpringBootTest
@AutoConfigureMockMvc
class CoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoinService service;

    @MockitoBean
    private CoinRepository repository;

    @Test
    void shouldReturnCoins() throws Exception {

        CoinResponseDto dto = new CoinResponseDto();
        dto.setName("Bitcoin");
        dto.setSymbol("BTC");
        dto.setCoingeckoId("coingeckoId_btc");
        when(service.getAllCoins())
                .thenReturn(List.of(dto));
        mockMvc.perform(get("/api/v1/coins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Bitcoin"));
    }

    @Test
    void shouldReturnCoin() throws Exception {

        CoinResponseDto dto = new CoinResponseDto();
        dto.setName("Bitcoin");

        when(service.getCoin(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/coins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Bitcoin"));
    }
}