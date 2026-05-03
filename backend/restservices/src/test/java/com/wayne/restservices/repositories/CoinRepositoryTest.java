package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Coin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CoinRepositoryTest {

    @Autowired
    private CoinRepository repository;

    @Test
    void shouldSaveCoin() {

        Coin coin = new Coin();
        coin.setName("Bitcoin");

        Coin saved = repository.save(coin);

        assertEquals("Bitcoin",
                saved.getName());
    }
}