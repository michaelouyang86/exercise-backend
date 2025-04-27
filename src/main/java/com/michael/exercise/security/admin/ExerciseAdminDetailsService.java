package com.michael.exercise.security.admin;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.Admin;
import com.michael.exercise.repositories.AdminRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ExerciseAdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public ExerciseAdminDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.getAdminByUsername(username);

        Set<GrantedAuthority> authorities = 
            Set.of(new SimpleGrantedAuthority("ADMIN"));

        return ExerciseAdminDetails.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities(authorities)
                .build();
    }
}
