package com.michael.exercise.security.admin;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class AdminDaoAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    public boolean supports(Class<?> authentication) {
        return AdminAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
