package com.wayne.restservices.exceptions;

import com.wayne.restservices.dtos.ErrorResponseDto;
import com.wayne.restservices.dtos.ValidationErrorDto;
import com.wayne.restservices.dtos.ValidationErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        return new ErrorResponseDto(
                Instant.now(),
                500,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CoinNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleCoinNotFoundException(CoinNotFoundException ex,
                                                        HttpServletRequest request){
        return new ErrorResponseDto(
                Instant.now(),
                404,
                "Coin not found",
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponseDto handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        List<ValidationErrorDto> errors =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> new ValidationErrorDto(
                                error.getField(),
                                error.getDefaultMessage()
                        ))
                        .toList();

        return new ValidationErrorResponseDto(
                Instant.now(),
                400,
                "Validation Failed",
                "Request validation failed",
                request.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(CoinAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleCoinAlreadyExists(
            CoinAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        return new ErrorResponseDto(
                Instant.now(),
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleCategoryNotFoundException(
            CategoryNotFoundException ex,
            HttpServletRequest request
    ) {
        return new ErrorResponseDto(
                Instant.now(),
                404,
                "Category not found",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleCategoryAlreadyExists(
            CategoryAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        return new ErrorResponseDto(
                Instant.now(),
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
    }
}
