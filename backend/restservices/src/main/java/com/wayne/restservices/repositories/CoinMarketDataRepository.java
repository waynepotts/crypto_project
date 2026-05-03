package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.CoinMarketData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinMarketDataRepository extends JpaRepository<CoinMarketData, Long> {

    List<CoinMarketData> findByCoinIdOrderByLastUpdatedDesc(Long coinId);

}