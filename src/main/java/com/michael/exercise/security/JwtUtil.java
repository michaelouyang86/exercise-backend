package com.michael.exercise.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtUtil {

    private static final SecretKey SECRET_KEY = createSecretKey();
    private static final JwtParser JWT_PARSER = createJwtParser();

    private static SecretKey createSecretKey() {
        String jwtSecret = System.getenv("JWT_SECRET");

        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("Environment variable 'JWT_SECRET' is not set or empty.");
        }

        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private static JwtParser createJwtParser() {
        return Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build();
    }

    public static String generateToken(int id, String role) {
        Instant now = Instant.now();

        return Jwts.builder()
            .issuer("exercise")
            .subject(String.valueOf(id))
            .claim("role", role)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(12, ChronoUnit.HOURS)))
            .signWith(SECRET_KEY)
            .compact();
    }

    public static Claims validateTokenAndGetClaims(String token) {
        return JWT_PARSER.parseSignedClaims(token).getPayload();
    }
}
