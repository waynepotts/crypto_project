package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public class BuildInfoResponseDto {

    @Schema(
            description = "The name of this application",
            example = "restservices"
    )
    private String application;

    @Schema(
            description = "version number",
            example = "0.0.1"
    )
    private String version;

    @Schema(
            description = "Timestamp when this application was built",
            example = "2026-05-08T13:49:51.076Z"
    )
    private Instant buildTime;

    public BuildInfoResponseDto(String application,
                             String version,
                             Instant buildTime) {
        this.application = application;
        this.version = version;
        this.buildTime = buildTime;
    }

    public String getApplication() {
        return application;
    }

    public String getVersion() {
        return version;
    }

    public Instant getBuildTime() {
        return buildTime;
    }
}
