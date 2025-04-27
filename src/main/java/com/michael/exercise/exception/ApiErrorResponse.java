package com.michael.exercise.exception;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiErrorResponse {
    private String code;
    private String message;
}
