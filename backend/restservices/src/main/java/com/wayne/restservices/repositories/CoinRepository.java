package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {

    Optional<Coin> findByCoingeckoId(String coingeckoId);

    boolean existsByCoingeckoId(String coingeckoId);

    boolean existsBySymbolIgnoreCase(String symbol);

}
