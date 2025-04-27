package com.michael.exercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) throws JsonProcessingException {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        String responseBody = objectMapper.writeValueAsString(apiErrorResponse);
        log.error("Response body: {}", responseBody);
        return ResponseEntity
                .status(ex.getStatus())
                .body(apiErrorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.error("{} - Login failed: {}", ex.getClass(), ex.getMessage());
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code("error")
                .message(status.getReasonPhrase())
                .build();
        return ResponseEntity
                .status(status)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        log.error("Stack trace: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code("error")
                .message(status.getReasonPhrase())
                .build();
        return ResponseEntity
                .status(status)
                .body(apiErrorResponse);
    }
}
