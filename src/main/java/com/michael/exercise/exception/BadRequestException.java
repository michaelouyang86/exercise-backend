package com.michael.exercise.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BadRequestException extends ApiException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}
