package com.wayne.entities.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {

    Optional<Coin> findByCoingeckoId(String coingeckoId);

}
