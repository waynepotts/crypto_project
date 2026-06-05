package com.wayne.restservices.dtos;

import org.springframework.data.domain.Page;

public class CoinHistoryPagedResponseDto extends PagedResponseDto<CoinHistoryPointDto> {

    private Long coinId;

    private String coinName;
    private double completeness;

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
