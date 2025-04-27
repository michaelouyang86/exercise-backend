package com.michael.exercise.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class NotFoundException extends ApiException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause, HttpStatus.NOT_FOUND);
    }
}
