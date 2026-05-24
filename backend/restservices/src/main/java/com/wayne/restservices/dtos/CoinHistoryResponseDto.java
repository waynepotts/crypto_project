package com.wayne.restservices.dtos;

import java.util.List;

public class CoinHistoryResponseDto {

    List<CoinHistoryPointDto> chartData;
    Double completeness;
    CoinResponseDto coinDto;

    public List<CoinHistoryPointDto> getChartData() {
        return chartData;
    }

    public void setChartData(List<CoinHistoryPointDto> chartData) {
        this.chartData = chartData;
    }

    public Double getCompleteness() { return completeness; }

    public void setCompleteness(Double completeness) { this.completeness = completeness; }

    public CoinResponseDto getCoinDto() {
        return coinDto;
    }

    public void setCoinDto(CoinResponseDto coinDto) {
        this.coinDto = coinDto;
    }
}
