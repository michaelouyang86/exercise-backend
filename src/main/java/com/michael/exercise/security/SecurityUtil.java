package com.michael.exercise.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtil {

    public static int getUserId() {
        Authentication authentication = 
            SecurityContextHolder.getContext().getAuthentication();

        return Integer.parseInt(authentication.getName());
    }
}
