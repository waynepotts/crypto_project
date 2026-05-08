package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCoinRequestDto {

    @Schema(
            description = "CoinGecko ID",
            example = "bitcoin"
    )
    @NotBlank
    @Size(min = 1, max = 255)
    private String coingeckoId;

    @Schema(
            description = "Coin symbol",
            example = "BTC"
    )
    @NotBlank
    @Size(min = 2, max = 255)
    private String symbol;

    @Schema(
            description = "Display name",
            example = "Bitcoin"
    )
    @NotBlank
    @Size(max = 255)
    private String name;

    @Schema(
            description = "URL to the logo image",
            example = "www.url.com/btc.jpg"
    )
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
