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
    SELECT md FROM CoinMarketData md
    WHERE md.coin.id = :coinId
    AND md.lastUpdated = :lastUpdated
    """)
    CoinMarketData findByCoinIdLastUpdated(Long coinId, Instant lastUpdated);
    @Query("""
    SELECT md FROM CoinMarketData md
    WHERE md.coin.id = :coinId
    AND md.lastUpdated = :lastUpdated
    """)
    List<CoinMarketData> findByCoinIdLastUpdatedList(Long coinId, Instant lastUpdated);
    /*@Query("""
    SELECT md
    FROM CoinMarketData md
    WHERE md.marketCapRank BETWEEN :lowest and :highest
    ORDER BY md.createdAt DESC LIMIT :limit
""")*/
    @Query("""
    SELECT md1 FROM CoinMarketData md1
    WHERE md1.id IN
    (SELECT md.id
    FROM CoinMarketData md
    WHERE md.marketCapRank BETWEEN :lowest and :highest
    ORDER BY md.createdAt DESC LIMIT :limit)
    ORDER BY md1.marketCapRank
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
            @Param("granularity") ChronoUnit granularity,
            Pageable pageable
    );

}