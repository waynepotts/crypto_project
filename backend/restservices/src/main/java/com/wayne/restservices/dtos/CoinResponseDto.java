package com.wayne.restservices.dtos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CoinResponseDto {
    private Long id;
    private String coingeckoId;
    private String symbol;
    private String name;
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoingeckoId() {
        return coingeckoId;
    }

    public void setCoingeckoId(String coingeckoId) {
        this.coingeckoId = coingeckoId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CoinResponseDto that = (CoinResponseDto) o;

        return new EqualsBuilder().append(getId(), that.getId()).append(getCoingeckoId(), that.getCoingeckoId()).append(getSymbol(), that.getSymbol()).append(getName(), that.getName()).append(getImage(), that.getImage()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getCoingeckoId()).append(getSymbol()).append(getName()).append(getImage()).toHashCode();
    }
}
