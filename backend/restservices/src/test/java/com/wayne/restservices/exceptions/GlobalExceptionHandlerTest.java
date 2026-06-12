package com.wayne.restservices.exceptions;

import com.wayne.restservices.dtos.ErrorResponseDto;
import com.wayne.restservices.dtos.ValidationErrorDto;
import com.wayne.restservices.dtos.ValidationErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldHandleGenericException() {
        when(request.getRequestURI()).thenReturn("/api/v1/test");

        ErrorResponseDto response = handler.handleException(
                new RuntimeException("Unexpected error"), request);

        assertEquals(500, response.status());
        assertEquals("Internal Server Error", response.error());
        assertEquals("Unexpected error", response.message());
        assertEquals("/api/v1/test", response.path());
        assertNotNull(response.timestamp());
    }

    @Test
    void shouldHandleCoinNotFoundException() {
        when(request.getRequestURI()).thenReturn("/api/v1/coins/999");

        ErrorResponseDto response = handler.handleCoinNotFoundException(
                new CoinNotFoundException(999L), request);

        assertEquals(404, response.status());
        assertEquals("Coin not found", response.error());
        assertEquals("Coin not found with id: 999", response.message());
        assertEquals("/api/v1/coins/999", response.path());
    }

    @Test
    void shouldHandleCoinAlreadyExistsException() {
        when(request.getRequestURI()).thenReturn("/api/v1/coins");

        ErrorResponseDto response = handler.handleCoinAlreadyExists(
                new CoinAlreadyExistsException("bitcoin"), request);

        assertEquals(409, response.status());
        assertEquals("Conflict", response.error());
        assertEquals("Coin already exists with CoinGecko ID, or symbol: bitcoin", response.message());
        assertEquals("/api/v1/coins", response.path());
    }

    @Test
    void shouldHandleValidationException() {
        when(request.getRequestURI()).thenReturn("/api/v1/coins");

        Object target = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "target");
        bindingResult.addError(new FieldError("target", "name", "Name is required"));
        bindingResult.addError(new FieldError("target", "symbol", "Symbol is required"));
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ValidationErrorResponseDto response =
                handler.handleValidationException(ex, request);

        assertEquals(400, response.status());
        assertEquals("Validation Failed", response.error());
        assertEquals("Request validation failed", response.message());
        assertEquals("/api/v1/coins", response.path());
        assertNotNull(response.timestamp());

        assertEquals(2, response.validationErrors().size());
        assertTrue(response.validationErrors().stream()
                .map(ValidationErrorDto::field)
                .anyMatch(f -> f.equals("name")));
        assertTrue(response.validationErrors().stream()
                .map(ValidationErrorDto::field)
                .anyMatch(f -> f.equals("symbol")));
    }
}
