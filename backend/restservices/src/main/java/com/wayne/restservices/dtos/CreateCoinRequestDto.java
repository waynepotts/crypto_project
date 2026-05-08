package com.wayne.restservices.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCoinRequestDto {

    @NotBlank
    @Size(min = 1, max = 255)
    private String coingeckoId;

    @NotBlank
    @Size(min = 2, max = 255)
    private String symbol;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    private String image;

    public @NotBlank @Size(min = 1, max = 255) String getCoingeckoId() {
        return coingeckoId;
    }

    public void setCoingeckoId(@NotBlank @Size(min = 1, max = 255) String coingeckoId) {
        this.coingeckoId = coingeckoId;
    }

    public @NotBlank @Size(min = 1, max = 255) @NotBlank @Size(min = 2, max = 10) String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NotBlank @Size(min = 1, max = 255) @NotBlank @Size(min = 2, max = 10) String symbol) {
        this.symbol = symbol;
    }

    public @NotBlank @Size(max = 255) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(max = 100) String name) {
        this.name = name;
    }

    public @NotBlank @Size(min = 1, max = 255) String getImage() {
        return image;
    }

    public void setImage(@NotBlank @Size(min = 1, max = 255) String image) {
        this.image = image;
    }
}
