package com.wayne.restservices.dtos.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeveloperDataDto(

        Integer forks,
        Integer stars,
        Integer subscribers,

        @JsonProperty("total_issues")
        Integer totalIssues,

        @JsonProperty("closed_issues")
        Integer closedIssues,

        @JsonProperty("pull_requests_merged")
        Integer pullRequestsMerged,

        @JsonProperty("pull_request_contributors")
        Integer pullRequestContributors,

        @JsonProperty("code_additions_deletions_4_weeks")
        CodeActivityDto codeAdditionsDeletions4Weeks,

        @JsonProperty("commit_count_4_weeks")
        Integer commitCount4Weeks
) {
}
