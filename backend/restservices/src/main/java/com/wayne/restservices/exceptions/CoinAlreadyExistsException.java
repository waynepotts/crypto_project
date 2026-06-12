package com.wayne.restservices.exceptions;

public class CoinAlreadyExistsException extends RuntimeException {
    public CoinAlreadyExistsException(String coingeckoId) {
        super("Coin already exists with CoinGecko ID, or symbol: " + coingeckoId);
    }
}
