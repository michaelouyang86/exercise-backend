package com.michael.exercise.security.admin;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AdminAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public AdminAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
