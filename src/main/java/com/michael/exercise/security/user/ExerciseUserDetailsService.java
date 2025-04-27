package com.michael.exercise.security.user;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.User;
import com.michael.exercise.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ExerciseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public ExerciseUserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.getUserByPhone(phone);

        Set<GrantedAuthority> authorities = 
            Set.of(new SimpleGrantedAuthority(user.getRole()));

        return ExerciseUserDetails.builder()
                .id(user.getId())
                .username(user.getPhone())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
