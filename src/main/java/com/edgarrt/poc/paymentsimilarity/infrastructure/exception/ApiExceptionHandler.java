package com.edgarrt.poc.paymentsimilarity.infrastructure.exception;

import com.edgarrt.poc.paymentsimilarity.domain.exception.DomainException;
import com.edgarrt.poc.paymentsimilarity.infrastructure.adapter.out.qdrant.InfrastructureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid request payload.",
                details
        ));
    }

    @ExceptionHandler({DomainException.class, IllegalArgumentException.class})
    ResponseEntity<ApiErrorResponse> handleDomain(RuntimeException exception) {
        return ResponseEntity.badRequest().body(ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                List.of()
        ));
    }

    @ExceptionHandler(InfrastructureException.class)
    ResponseEntity<ApiErrorResponse> handleInfrastructure(InfrastructureException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ApiErrorResponse.of(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                exception.getMessage(),
                List.of()
        ));
    }
}
