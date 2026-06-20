package com.wayne.restservices.dtos;

import java.io.Serializable;
import java.util.List;

public record CoinHistoryResponseDto(
    List<CoinHistoryPointDto> chartData,
    Double completeness,
    CoinResponseDto coinDto
) implements Serializable {}
