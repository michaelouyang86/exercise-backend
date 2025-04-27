package com.michael.exercise.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final String code;
    private final HttpStatus status;
    
    public ApiException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
    
    public ApiException(String code, String message,
                        Throwable cause, HttpStatus status) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {

        private String code;
        private String message;
        private Throwable cause;
        private HttpStatus status;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }
        
        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ApiException build() {
            return new ApiException(code, message, cause, status);
        }
    }
}
