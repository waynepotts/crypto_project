package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Coin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CoinRepositoryTest {

    @Autowired
    private CoinRepository repository;

    @Test
    void shouldSaveCoin() {
        Coin coin = new Coin();
        coin.setName("Bitcoin");
        coin.setCoingeckoId("test_coingecko_BTC");
        coin.setSymbol("BTC");
        Coin saved = repository.save(coin);
        assertEquals("Bitcoin", saved.getName());
    }

    @Test
    void shouldFindCoin() {
        Coin coin = new Coin();
        coin.setName("Bitcoin");
        coin.setCoingeckoId("test_coingecko_BTC");
        coin.setSymbol("BTC");
        Coin saved = repository.save(coin);

        Coin read = repository.findById(saved.getId()).get();
        assertEquals(saved, read);
    }

    @Test
    void shouldFindCoinByCoingeckoId() {
        Coin coin = new Coin();
        coin.setName("Bitcoin");
        coin.setCoingeckoId("test_coingecko_BTC");
        coin.setSymbol("BTC");
        Coin saved = repository.save(coin);

        Coin read = repository.findByCoingeckoId(saved.getCoingeckoId()).get();
        assertEquals(saved, read);
    }

    @Test
    void shouldSearchCoinsByNameContaining() {
        repository.save(createCoin("Bitcoin", "BTC", "test_search_btc"));

        Page<Coin> results = repository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "bit", "bit", PageRequest.of(0, 10));

        assertFalse(results.isEmpty());
        assertTrue(results.getContent().stream().anyMatch(c -> c.getName().equals("Bitcoin")));
    }

    @Test
    void shouldSearchCoinsBySymbolContaining() {
        repository.save(createCoin("Ethereum", "ETH", "test_search_eth"));

        Page<Coin> results = repository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "eth", "eth", PageRequest.of(0, 10));

        assertFalse(results.isEmpty());
        assertTrue(results.getContent().stream().anyMatch(c -> c.getSymbol().equals("ETH")));
    }

    @Test
    void shouldReturnEmptyPageWhenNoSearchMatch() {
        repository.save(createCoin("Bitcoin", "BTC", "test_search_empty"));

        Page<Coin> results = repository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "xyz", "xyz", PageRequest.of(0, 10));

        assertTrue(results.isEmpty());
    }

    @Test
    void shouldSearchCoinsCaseInsensitive() {
        repository.save(createCoin("Bitcoin", "BTC", "test_search_case"));

        Page<Coin> results = repository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                "BITCOIN", "BITCOIN", PageRequest.of(0, 10));

        assertFalse(results.isEmpty());
        assertTrue(results.getContent().stream().anyMatch(c -> c.getName().equals("Bitcoin")));
    }

    private Coin createCoin(String name, String symbol, String coingeckoId) {
        Coin coin = new Coin();
        coin.setName(name);
        coin.setSymbol(symbol);
        coin.setCoingeckoId(coingeckoId);
        return coin;
    }
}