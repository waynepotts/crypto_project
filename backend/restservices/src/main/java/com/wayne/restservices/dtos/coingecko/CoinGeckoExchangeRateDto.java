package com.wayne.restservices.dtos.coingecko;

import java.math.BigDecimal;


public record CoinGeckoExchangeRateDto(String name, String unit, BigDecimal value, String type) {

}
