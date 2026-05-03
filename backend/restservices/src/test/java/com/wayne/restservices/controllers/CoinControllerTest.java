package com.wayne.restservices.controllers;

import com.wayne.restservices.entities.dto.CoinResponseDto;
import com.wayne.restservices.services.CoinService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoinController.class)
class CoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoinService service;

    @Test
    void shouldReturnCoins() throws Exception {

        CoinResponseDto dto = new CoinResponseDto();
        dto.setName("Bitcoin");

        when(service.getAllCoins())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/coins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Bitcoin"));
    }
}