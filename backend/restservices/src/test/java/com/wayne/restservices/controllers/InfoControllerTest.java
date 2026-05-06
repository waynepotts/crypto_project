package com.wayne.restservices.controllers;

import com.wayne.restservices.entities.dto.BuildInfoResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BuildProperties buildProperties;

    @MockitoBean
    private InfoController infoController;

    @Test
    void shouldReturnInfo() throws Exception {

        BuildInfoResponseDto dto = new BuildInfoResponseDto("info", "info", Instant.now());
        when(infoController.getBuildInfo()).thenReturn(dto);

        mockMvc.perform(get("/api/v1/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application")
                        .value("info"));

    }
}
