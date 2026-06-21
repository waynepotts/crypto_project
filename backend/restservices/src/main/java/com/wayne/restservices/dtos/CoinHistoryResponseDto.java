package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

public record CoinHistoryResponseDto(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<CoinHistoryPointDto> chartData,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Double completeness,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CoinResponseDto coinDto
) implements Serializable {}
