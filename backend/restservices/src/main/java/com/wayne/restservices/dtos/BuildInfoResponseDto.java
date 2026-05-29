package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record BuildInfoResponseDto(
    @Schema(description = "The name of this application", example = "restservices") String application,
    @Schema(description = "version number", example = "0.0.1") String version,
    @Schema(description = "Timestamp when this application was built", example = "2026-05-08T13:49:51.076Z") Instant buildTime
) {}
