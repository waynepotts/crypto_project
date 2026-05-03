package com.wayne.restservices.entities.dto;

public class CoinDto {
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
}
