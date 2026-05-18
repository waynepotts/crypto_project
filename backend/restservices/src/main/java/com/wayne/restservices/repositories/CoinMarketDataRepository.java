package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Coin;
import com.wayne.restservices.entities.jpa.CoinMarketData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface CoinMarketDataRepository extends JpaRepository<CoinMarketData, Long> {

    List<CoinMarketData> findByCoinIdOrderByLastUpdatedDesc(Long coinId);

    Page<CoinMarketData> findByMarketCapRankBetweenAndCreatedAtIsNear(Integer marketCapRankAfter, Integer marketCapRankBefore, Instant createdAt,
                                                                                           Pageable pageable);

    CoinMarketData findFirstByCoinIdOrderByLastUpdatedDesc(Long coinId);

    @Query("""
    SELECT md 
    FROM CoinMarketData md 
    WHERE md.marketCapRank BETWEEN :lowest and :highest
    ORDER BY md.createdAt DESC LIMIT :limit
""")
    List<CoinMarketData> findLatestMarketCapRankRange(@Param("lowest") Integer lowest, @Param("highest") Integer highest,  @Param("limit") Integer limit);

    @Query("""
    SELECT md
    FROM CoinMarketData md
    WHERE md.coin = :coin
      AND md.lastUpdated BETWEEN :from AND :to
      AND md.granularity >= :granularity
""")
    Page<CoinMarketData> findByCoinLastUpdatedRange(
            @Param("coin") Coin coin,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("granularity") Short granularity,
            Pageable pageable
    );

}