package com.wayne.restservices.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cryptoApi() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Crypto REST API")
                                .description(
                                        "REST API for cryptocurrency data"
                                )
                                .version("v1")
                );
    }
}