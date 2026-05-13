package com.wayne.restservices.validators;

import com.wayne.restservices.dtos.coingecko.CoinGeckoCoinDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CoinMarketDataValidator {
    public void validate(CoinGeckoCoinDto dto) {

        requireNonNull(dto.getId(), "id");
        requireNonNull(dto.getCurrentPrice(), "current_price");

        validatePercentage(dto.getPriceChangePercentage24h(), "price_change_percentage_24h");
        validatePercentage(dto.getMarketCapChangePercentage24h(), "market_cap_change_percentage_24h");
        validatePercentage(dto.getAthChangePercentage(), "ath_change_percentage");
        validatePercentage(dto.getAtlChangePercentage(), "atl_change_percentage");

        validateNonNegative(dto.getMarketCap(), "market_cap");
    }

    private void validatePercentage(BigDecimal value, String field) {
        if (value == null) return;

        if (value.abs().compareTo(new BigDecimal("1000000")) > 0) {
            throw new IllegalArgumentException(
                    field + " is unreasonably large: " + value
            );
        }
    }

    private void validateNonNegative(Number value, String field) {
        if (value == null) return;

        if (value.longValue() < 0) {
            throw new IllegalArgumentException(
                    field + " cannot be negative: " + value
            );
        }
    }

    private void requireNonNull(Object value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " cannot be null");
        }
    }
}
