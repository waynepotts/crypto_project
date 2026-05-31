package com.wayne.restservices.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void cryptoApiShouldReturnNonNullOpenApi() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.cryptoApi();

        assertNotNull(openAPI);
    }

    @Test
    void cryptoApiShouldSetCorrectTitle() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.cryptoApi();

        assertEquals("Crypto REST API", openAPI.getInfo().getTitle());
    }

    @Test
    void cryptoApiShouldSetCorrectDescription() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.cryptoApi();

        assertEquals("REST API for cryptocurrency data", openAPI.getInfo().getDescription());
    }

    @Test
    void cryptoApiShouldSetCorrectVersion() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.cryptoApi();

        assertEquals("v1", openAPI.getInfo().getVersion());
    }
}
