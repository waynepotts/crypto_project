package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record ErrorResponseDto(
    @Schema(description = "Timestamp when the error was made", example = "2026-05-08T13:49:51.076Z") Instant timestamp,
    @Schema(description = "status code for the error", example = "500") int status,
    @Schema(description = "Error type description", example = "Internal Server Error") String error,
    @Schema(description = "What caused the error", example = "No static resource api/v1/infoooo") String message,
    @Schema(description = "Path that caused the error", example = "/api/v1/infoooo") String path
) {}
