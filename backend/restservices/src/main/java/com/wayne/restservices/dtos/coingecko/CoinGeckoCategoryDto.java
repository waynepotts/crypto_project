package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoinGeckoCategoryDto(
        @JsonProperty("category_id")
        String categoryId,
        @JsonProperty("name")
        String name
)
{}
