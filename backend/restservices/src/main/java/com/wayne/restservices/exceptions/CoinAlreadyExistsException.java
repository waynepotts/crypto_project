package com.wayne.restservices.exceptions;

public class CoinAlreadyExistsException extends RuntimeException {
    public CoinAlreadyExistsException(String coingeckoId) {
        super("Coing eck id " + coingeckoId + " already exists");
    }
}
