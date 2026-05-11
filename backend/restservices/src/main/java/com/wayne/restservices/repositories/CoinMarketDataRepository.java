package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CoinMarketDataRepository extends JpaRepository<CoinMarketData, Long> {

    List<CoinMarketData> findByCoinIdOrderByLastUpdatedDesc(Long coinId);

    CoinMarketData findFirstByCoinIdOrderByLastUpdatedDesc(Long coinId);

    @Query("""
    SELECT md
    FROM CoinMarketData md
    WHERE md.coin = :coin
      AND md.lastUpdated BETWEEN :from AND :to
""")
    Page<CoinMarketData> findByCoinLastUpdatedRange(
            @Param("coin") Coin coin,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable
    );

}