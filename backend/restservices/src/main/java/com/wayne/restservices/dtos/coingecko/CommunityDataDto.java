package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CommunityDataDto(

        @JsonProperty("facebook_likes")
        Long facebookLikes,

        @JsonProperty("reddit_average_posts_48h")
        BigDecimal redditAveragePosts48h,

        @JsonProperty("reddit_average_comments_48h")
        BigDecimal redditAverageComments48h,

        @JsonProperty("reddit_subscribers")
        Long redditSubscribers,

        @JsonProperty("reddit_accounts_active_48h")
        BigDecimal redditAccountsActive48h,

        @JsonProperty("telegram_channel_user_count")
        Long telegramChannelUserCount
) {
}
