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

        ValidationErrorResponseDto response =
                new ValidationErrorResponseDto();

        response.setTimestamp(Instant.now());
        response.setStatus(400);
        response.setError("Validation Failed");
        response.setMessage("Request validation failed");
        response.setPath(request.getRequestURI());
        response.setValidationErrors(errors);

        return response;
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
}
