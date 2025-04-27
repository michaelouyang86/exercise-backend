package com.michael.exercise.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RestController;

import com.michael.exercise.dtos.AdminTokenResponse;
import com.michael.exercise.dtos.GetAdminTokenRequest;
import com.michael.exercise.dtos.GetUserTokenRequest;
import com.michael.exercise.dtos.UserTokenResponse;
import com.michael.exercise.security.JwtUtil;
import com.michael.exercise.security.admin.AdminAuthenticationToken;
import com.michael.exercise.security.admin.ExerciseAdminDetails;
import com.michael.exercise.security.user.ExerciseUserDetails;
import com.michael.exercise.security.user.UserAuthenticationToken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationManager authenticationManager;
    
    @Override
    public ResponseEntity<UserTokenResponse> getUserToken(GetUserTokenRequest request) {
        String phone = request.getPhone();
        String password = request.getPassword();
        UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(phone, password);

        log.info("User login attempt with phone number: {}", phone);
        Authentication authentication = authenticationManager.authenticate(userAuthenticationToken);
        ExerciseUserDetails exerciseUserDetails = (ExerciseUserDetails) authentication.getPrincipal();
        
        UserTokenResponse response = new UserTokenResponse();
        String role = exerciseUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElseThrow();
        response.setRole(UserTokenResponse.RoleEnum.fromValue(role));
        response.setToken(JwtUtil.generateToken(exerciseUserDetails.getId(), role));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<AdminTokenResponse> getAdminToken(GetAdminTokenRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        AdminAuthenticationToken adminAuthenticationToken = new AdminAuthenticationToken(username, password);

        log.info("Admin login attempt with username: {}", username);
        Authentication authentication = authenticationManager.authenticate(adminAuthenticationToken);
        ExerciseAdminDetails exerciseAdminDetails = (ExerciseAdminDetails) authentication.getPrincipal();
        
        AdminTokenResponse response = new AdminTokenResponse();
        String role = exerciseAdminDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElseThrow();
        response.setToken(JwtUtil.generateToken(exerciseAdminDetails.getId(), role));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
