package com.wayne.restservices.dtos.coingecko;

import java.util.List;

public record ReposDto(
        List<String> github,
        List<String> bitbucket
) {
}
