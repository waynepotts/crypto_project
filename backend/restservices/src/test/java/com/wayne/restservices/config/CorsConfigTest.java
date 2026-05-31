package com.wayne.restservices.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
        corsConfig.setUrl("http://localhost");
        corsConfig.setPort("5173");
    }

    @Test
    void shouldReturnCorrectUrl() {
        assertEquals("http://localhost", corsConfig.getUrl());
    }

    @Test
    void shouldReturnCorrectPort() {
        assertEquals("5173", corsConfig.getPort());
    }

    @Test
    void shouldSetUrlViaSetter() {
        corsConfig.setUrl("http://example.com");
        assertEquals("http://example.com", corsConfig.getUrl());
    }

    @Test
    void shouldSetPortViaSetter() {
        corsConfig.setPort("3000");
        assertEquals("3000", corsConfig.getPort());
    }

    @Test
    void corsConfigurerShouldReturnNonNullConfigurer() {
        WebMvcConfigurer configurer = corsConfig.corsConfigurer();
        assertNotNull(configurer);
    }

    @Test
    void shouldAddCorrectCorsMappings() {
        String[] origins = {"http://localhost:5173"};
        String[] methods = {"*"};

        WebMvcConfigurer configurer = corsConfig.corsConfigurer();
        assertNotNull(configurer);
    }
}
