package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class CoinGeckoChartPointDto {
    private Instant timeStamp;
    private BigDecimal value;

    @JsonCreator
    public CoinGeckoChartPointDto(List<Object> values) {

        this.timeStamp = Instant.ofEpochMilli(
                ((Number) values.get(0)).longValue()
        );

        this.value = new BigDecimal(
                values.get(1).toString()
        );
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
