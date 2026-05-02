package com.wayne.entities.dto;

import java.time.Instant;

public class BuildInfoResponseDto {

    private String application;
    private String version;
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
