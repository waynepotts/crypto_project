package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LinksDto(

        List<String> homepage,

        String whitepaper,

        @JsonProperty("blockchain_site")
        List<String> blockchainSite,

        @JsonProperty("official_forum_url")
        List<String> officialForumUrl,

        @JsonProperty("chat_url")
        List<String> chatUrl,

        @JsonProperty("announcement_url")
        List<String> announcementUrl,

        @JsonProperty("snapshot_url")
        String snapshotUrl,

        @JsonProperty("twitter_screen_name")
        String twitterScreenName,

        @JsonProperty("facebook_username")
        String facebookUsername,

        @JsonProperty("telegram_channel_identifier")
        String telegramChannelIdentifier,

        @JsonProperty("subreddit_url")
        String subredditUrl,

        @JsonProperty("repos_url")
        ReposDto reposUrl
) {
}
