package com.wayne.restservices.dtos;

public record ValidationErrorDto(
    String field,
    String message
) {}
