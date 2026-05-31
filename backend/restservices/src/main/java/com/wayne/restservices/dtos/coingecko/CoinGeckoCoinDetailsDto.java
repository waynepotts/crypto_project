package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record CoinGeckoCoinDetailsDto(

        String id,
        String symbol,
        String name,

        @JsonProperty("web_slug")
        String webSlug,

        @JsonProperty("asset_platform_id")
        String assetPlatformId,

        Map<String, String> platforms,

        @JsonProperty("detail_platforms")
        Map<String, DetailPlatformDto> detailPlatforms,

        @JsonProperty("block_time_in_minutes")
        Integer blockTimeInMinutes,

        @JsonProperty("hashing_algorithm")
        String hashingAlgorithm,

        List<String> categories,

        @JsonProperty("preview_listing")
        Boolean previewListing,

        @JsonProperty("public_notice")
        String publicNotice,

        @JsonProperty("additional_notices")
        List<String> additionalNotices,

        Map<String, String> localization,

        Map<String, String> description,

        LinksDto links,

        ImageDto image,

        @JsonProperty("country_origin")
        String countryOrigin,

        @JsonProperty("genesis_date")
        String genesisDate,

        @JsonProperty("sentiment_votes_up_percentage")
        BigDecimal sentimentVotesUpPercentage,

        @JsonProperty("sentiment_votes_down_percentage")
        BigDecimal sentimentVotesDownPercentage,

        @JsonProperty("watchlist_portfolio_users")
        Long watchlistPortfolioUsers,

        @JsonProperty("market_cap_rank")
        Integer marketCapRank,

        @JsonProperty("market_cap_rank_with_rehypothecated")
        Integer marketCapRankWithRehypothecated,

        @JsonProperty("market_data")
        CoinGeckoCoinDto marketData,

        @JsonProperty("community_data")
        CommunityDataDto communityData,

        @JsonProperty("developer_data")
        DeveloperDataDto developerData,

        @JsonProperty("last_updated")
        Instant lastUpdated,

        List<TickerDto> tickers
) {
}
