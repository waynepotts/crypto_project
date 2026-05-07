package com.wayne.restservices.exceptions;

public class CoinNotFoundException extends RuntimeException {

    public CoinNotFoundException(Long id) {
        super("Coin not found with id: " + id);
    }
}
