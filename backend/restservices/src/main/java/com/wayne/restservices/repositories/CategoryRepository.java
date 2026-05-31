package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Category;
import com.wayne.restservices.entities.jpa.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
    SELECT c
    FROM Category c
    WHERE c.coingeckoCategoryId = :coingeckoId
""")
    Category findByCoingeckoId(String coingeckoId);

    Category findByName(String name);

}
