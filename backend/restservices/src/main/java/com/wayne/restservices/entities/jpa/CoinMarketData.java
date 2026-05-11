package com.wayne.restservices.entities.jpa;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "coin_market_data",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_coin_timestamp",
                        columnNames = {
                                "coin_id",
                                "last_updated"
                        }
                )
        }
)
public class CoinMarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "coin_id",
            nullable = false
    )
    private Coin coin;

    @Column(
            name = "current_price",
            precision = 38,
            scale = 18
    )
    private BigDecimal currentPrice;

    @Column(name = "market_cap")
    private Long marketCap;

    @Column(name = "market_cap_rank")
    private Integer marketCapRank;

    @Column(name = "fully_diluted_valuation")
    private Long fullyDilutedValuation;

    @Column(name = "total_volume")
    private Long totalVolume;

    @Column(
            name = "high_24h",
            precision = 38,
            scale = 18
    )
    private BigDecimal high24h;

    @Column(
            name = "low_24h",
            precision = 38,
            scale = 18
    )
    private BigDecimal low24h;

    @Column(
            name = "price_change_24h",
            precision = 38,
            scale = 18
    )
    private BigDecimal priceChange24h;

    @Column(
            name = "price_change_percentage_24h",
            precision = 38,
            scale = 18
    )
    private BigDecimal priceChangePercentage24h;

    @Column(name = "market_cap_change_24h")
    private Long marketCapChange24h;

    @Column(
            name = "market_cap_change_percentage_24h",
            precision = 38,
            scale = 18
    )
    private BigDecimal marketCapChangePercentage24h;

    @Column(
            name = "circulating_supply",
            precision = 38,
            scale = 18
    )
    private BigDecimal circulatingSupply;

    @Column(
            name = "total_supply",
            precision = 38,
            scale = 18
    )
    private BigDecimal totalSupply;

    @Column(
            name = "max_supply",
            precision = 38,
            scale = 18
    )
    private BigDecimal maxSupply;

    @Column(
            name = "ath",
            precision = 38,
            scale = 18
    )
    private BigDecimal ath;

    @Column(
            name = "ath_change_percentage",
            precision = 38,
            scale = 18
    )
    private BigDecimal athChangePercentage;

    @Column(name = "ath_date")
    private Instant athDate;

    @Column(
            name = "atl",
            precision = 38,
            scale = 18
    )
    private BigDecimal atl;

    @Column(
            name = "atl_change_percentage",
            precision = 38,
            scale = 18
    )
    private BigDecimal atlChangePercentage;

    @Column(name = "atl_date")
    private Instant atlDate;

    @Column(
            name = "last_updated",
            nullable = false
    )
    private Instant lastUpdated;

    @Column(name = "market_cap_rank_with_rehypothecated")
    private Integer marketCapRankWithRehypothecated;


    @Column(name = "created_at", updatable = false)
    private Instant createdAt;



    @Column(name = "source", updatable = false)
    private String source;
    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    public Integer getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(Integer marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public Long getFullyDilutedValuation() {
        return fullyDilutedValuation;
    }

    public void setFullyDilutedValuation(Long fullyDilutedValuation) {
        this.fullyDilutedValuation = fullyDilutedValuation;
    }

    public Long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Long totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getHigh24h() {
        return high24h;
    }

    public void setHigh24h(BigDecimal high24h) {
        this.high24h = high24h;
    }

    public BigDecimal getLow24h() {
        return low24h;
    }

    public void setLow24h(BigDecimal low24h) {
        this.low24h = low24h;
    }

    public BigDecimal getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(BigDecimal priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public BigDecimal getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(
            BigDecimal priceChangePercentage24h
    ) {
        this.priceChangePercentage24h =
                priceChangePercentage24h;
    }

    public Long getMarketCapChange24h() {
        return marketCapChange24h;
    }

    public void setMarketCapChange24h(Long marketCapChange24h) {
        this.marketCapChange24h = marketCapChange24h;
    }

    public BigDecimal getMarketCapChangePercentage24h() {
        return marketCapChangePercentage24h;
    }

    public void setMarketCapChangePercentage24h(
            BigDecimal marketCapChangePercentage24h
    ) {
        this.marketCapChangePercentage24h =
                marketCapChangePercentage24h;
    }

    public BigDecimal getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(BigDecimal circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public BigDecimal getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(BigDecimal totalSupply) {
        this.totalSupply = totalSupply;
    }

    public BigDecimal getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(BigDecimal maxSupply) {
        this.maxSupply = maxSupply;
    }

    public BigDecimal getAth() {
        return ath;
    }

    public void setAth(BigDecimal ath) {
        this.ath = ath;
    }

    public BigDecimal getAthChangePercentage() {
        return athChangePercentage;
    }

    public void setAthChangePercentage(BigDecimal athChangePercentage) {
        this.athChangePercentage = athChangePercentage;
    }

    public Instant getAthDate() {
        return athDate;
    }

    public void setAthDate(Instant athDate) {
        this.athDate = athDate;
    }

    public BigDecimal getAtl() {
        return atl;
    }

    public void setAtl(BigDecimal atl) {
        this.atl = atl;
    }

    public BigDecimal getAtlChangePercentage() {
        return atlChangePercentage;
    }

    public void setAtlChangePercentage(BigDecimal atlChangePercentage) {
        this.atlChangePercentage = atlChangePercentage;
    }

    public Instant getAtlDate() {
        return atlDate;
    }

    public void setAtlDate(Instant atlDate) {
        this.atlDate = atlDate;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getMarketCapRankWithRehypothecated() {
        return marketCapRankWithRehypothecated;
    }

    public void setMarketCapRankWithRehypothecated(
            Integer marketCapRankWithRehypothecated
    ) {
        this.marketCapRankWithRehypothecated =
                marketCapRankWithRehypothecated;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}