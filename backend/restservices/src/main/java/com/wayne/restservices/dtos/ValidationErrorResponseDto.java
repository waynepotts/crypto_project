package com.wayne.restservices.dtos;

import java.time.Instant;
import java.util.List;

public class ValidationErrorResponseDto {

    private Instant timestamp;
    private int status;



    private String error;



    private String message;
    private String path;

    private List<ValidationErrorDto> validationErrors;

    public ValidationErrorResponseDto() {
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

    public List<ValidationErrorDto> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationErrorDto> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
