package com.wayne.restservices.dtos;

import org.springframework.data.domain.Page;

import java.io.Serializable;

public class CoinHistoryPagedResponseDto extends PagedResponseDto<CoinHistoryPointDto> implements Serializable {

    private Long coinId;

    private String coinName;
    private double completeness;

    public double getCompleteness() {
        return completeness;
    }

    public void setCompleteness(double completeness) {
        this.completeness = completeness;
    }

    public CoinHistoryPagedResponseDto(Page<CoinHistoryPointDto> paged) {
        super(paged);
    }

    public Long getCoinId() {
        return coinId;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}
