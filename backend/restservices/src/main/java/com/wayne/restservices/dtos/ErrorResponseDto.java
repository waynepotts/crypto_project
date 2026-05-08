package com.wayne.restservices.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public class ErrorResponseDto {

    @Schema(
            description = "Timestamp when the error was made",
            example = "2026-05-08T13:49:51.076Z"
    )
    private Instant timestamp;
    @Schema(
            description = "status code for the error",
            example = "500"
    )
    private int status;

    @Schema(
            description = "Error type description",
            example = "Internal Server Error"
    )
    private String error;

    @Schema(
            description = "What caused the error",
            example = "No static resource api/v1/infoooo"
    )
    private String message;

    @Schema(
            description = "Path that caused the error",
            example = "/api/v1/infoooo"
    )
    private String path;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}