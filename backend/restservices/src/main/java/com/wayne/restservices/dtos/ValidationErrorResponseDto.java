package com.wayne.restservices.dtos;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponseDto(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path,
    List<ValidationErrorDto> validationErrors
) {}
