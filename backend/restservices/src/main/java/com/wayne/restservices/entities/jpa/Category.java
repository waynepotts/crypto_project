package com.wayne.restservices.entities.jpa;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coingecko_category_id")
    private String coingeckoCategoryId;
    private String name;
    @ManyToMany(
            mappedBy = "categories",
            fetch = FetchType.LAZY
    )
    private Set<Coin> coins =
            new HashSet<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoingeckoCategoryId() {
        return coingeckoCategoryId;
    }

    public void setCoingeckoCategoryId(String coingeckoCategoryId) {
        this.coingeckoCategoryId = coingeckoCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Coin> getCoins() {
        return coins;
    }

    public void setCoins(Set<Coin> coins) {
        this.coins = coins;
    }
}
