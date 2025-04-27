package com.michael.exercise.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.michael.exercise.security.admin.AdminDaoAuthenticationProvider;
import com.michael.exercise.security.admin.ExerciseAdminDetailsService;
import com.michael.exercise.security.user.ExerciseUserDetailsService;
import com.michael.exercise.security.user.UserDaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                // Swagger
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/swagger-config").permitAll()
                .requestMatchers("/openapi/exercise.yaml").permitAll()

                // Public authentication endpoints
                .requestMatchers("/authentication/**").permitAll()

                // Role-based access control
                .requestMatchers("/student/**").hasRole("STUDENT")
                .requestMatchers("/teacher/**").hasRole("TEACHER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Block all other requests
                .anyRequest().denyAll()
            )
            .cors(config -> config.configurationSource(corsConfigurationSource()))
            .csrf(config -> config.disable())
            .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDaoAuthenticationProvider userDaoAuthenticationProvider, AdminDaoAuthenticationProvider adminDaoAuthenticationProvider) {
        List<AuthenticationProvider> providers = List.of(userDaoAuthenticationProvider, adminDaoAuthenticationProvider);
        return new ProviderManager(providers);
    }

    @Bean
    public UserDaoAuthenticationProvider userDaoAuthenticationProvider(ExerciseUserDetailsService exerciseUserDetailsService, PasswordEncoder passwordEncoder) {
        UserDaoAuthenticationProvider provider = new UserDaoAuthenticationProvider();
        provider.setUserDetailsService(exerciseUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AdminDaoAuthenticationProvider adminDaoAuthenticationProvider(ExerciseAdminDetailsService exerciseAdminDetailsService, PasswordEncoder passwordEncoder) {
        AdminDaoAuthenticationProvider provider = new AdminDaoAuthenticationProvider();
        provider.setUserDetailsService(exerciseAdminDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 1 << 14, 2);
    }
}
