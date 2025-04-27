package com.michael.exercise.security.user;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class UserDaoAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    public boolean supports(Class<?> authentication) {
        return UserAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
