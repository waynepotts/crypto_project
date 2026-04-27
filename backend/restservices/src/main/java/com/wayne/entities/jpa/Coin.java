package com.wayne.entities.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "coins")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coingecko_id", unique = true)
    private String coingeckoId;

    private String symbol;
    private String name;
    private String image;

    // getters/setters
    public Long getId() { return id; }
    public String getCoingeckoId() { return coingeckoId; }
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getImage() { return image; }

    public void setId(Long id) { this.id = id; }
    public void setCoingeckoId(String coingeckoId) { this.coingeckoId = coingeckoId; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setName(String name) { this.name = name; }
    public void setImage(String image) { this.image = image; }
}
