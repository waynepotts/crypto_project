package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Coin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CoinRepositoryTest {

    @Autowired
    private CoinRepository repository;

    private Coin createCoin(){
        Coin coin = new Coin();
        coin.setName("Bitcoin");
        coin.setCoingeckoId("coingeckoId_BTC");
        coin.setSymbol("Bitcoin");
        return coin;
    }
    @Test
    void shouldSaveCoin() {

        Coin coin = createCoin();
        Coin saved = repository.save(coin);

        assertEquals("Bitcoin",
                saved.getName());
    }

    @Test
    void shouldFindCoin() {
        Coin coin = createCoin();
        coin.setId(1L);
        Coin saved = repository.save(coin);

        Coin read = repository.findById(saved.getId()).get();
        assertEquals(saved, read);
        assertEquals(saved, coin);
    }

    @Test
    void shouldFindCoinByCoingeckoId() {
        Coin coin = createCoin();
        coin.setId(1L);
        Coin saved = repository.save(coin);

        Coin read = repository.findByCoingeckoId(saved.getCoingeckoId()).get();
        assertEquals(saved, read);
        assertEquals(saved, coin);
    }
}