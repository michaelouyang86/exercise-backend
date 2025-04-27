package com.michael.exercise.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UnauthenticatedException extends ApiException {

    private static final long serialVersionUID = 1L;
    public static final String CODE = "not_login";
    public static final String MESSAGE = "Invalid or expired token.";
    public static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public UnauthenticatedException() {
        super(CODE, MESSAGE, STATUS);
    }
}
