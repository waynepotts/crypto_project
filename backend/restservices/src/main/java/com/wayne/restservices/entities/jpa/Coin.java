package com.wayne.restservices.entities.jpa;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

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
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coin_categories",

            joinColumns = @JoinColumn(
                    name = "coin_id"
            ),

            inverseJoinColumns = @JoinColumn(
                    name = "category_id"
            )
    )
    private Set<Category> categories =
            new HashSet<>();

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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Coin coin = (Coin) o;

        return new EqualsBuilder().append(getId(), coin.getId()).append(getCoingeckoId(), coin.getCoingeckoId()).append(getSymbol(), coin.getSymbol()).append(getName(), coin.getName()).append(getImage(), coin.getImage()).append(getCategories(), coin.getCategories()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getCoingeckoId()).append(getSymbol()).append(getName()).append(getImage()).append(getCategories()).toHashCode();
    }
}
