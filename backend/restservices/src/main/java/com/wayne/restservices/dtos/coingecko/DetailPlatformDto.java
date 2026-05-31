package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DetailPlatformDto(
        @JsonProperty("decimal_place")
        Integer decimalPlace,

        @JsonProperty("contract_address")
        String contractAddress
) {
}
