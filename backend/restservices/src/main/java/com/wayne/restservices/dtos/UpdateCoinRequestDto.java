package com.wayne.restservices.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UpdateCoinRequestDto {

    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 2, max = 255)
    private String coingeckoId;
    @NotBlank
    @Size(min = 2, max = 255)
    private String symbol;
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;
    @NotBlank
    @Size(min = 2, max = 255)
    private String image;

    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public @NotBlank @Size(min = 2, max = 255) String getCoingeckoId() {
        return coingeckoId;
    }

    public void setCoingeckoId(@NotBlank @Size(min = 2, max = 255) String coingeckoId) {
        this.coingeckoId = coingeckoId;
    }

    public @NotBlank @Size(min = 2, max = 255) String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NotBlank @Size(min = 2, max = 255) String symbol) {
        this.symbol = symbol;
    }

    public @NotBlank @Size(min = 2, max = 255) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(min = 2, max = 255) String name) {
        this.name = name;
    }

    public @NotBlank @Size(min = 2, max = 255) String getImage() {
        return image;
    }

    public void setImage(@NotBlank @Size(min = 2, max = 255) String image) {
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
