package com.expense.expense_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "my-super-secret-key-my-super-secret-key-my-super-secret-key";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate token
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                )
                .signWith(key)
                .compact();
    }

    // Extract email
    public String extractUsername(String token) {

        return getClaims(token).getSubject();
    }

    // Validate token
    public boolean validateToken(String token, String email) {

        String tokenEmail = extractUsername(token);
        return tokenEmail.equals(email) && !isExpired(token);
    }

    // Check expiration
    private boolean isExpired(String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // Get claims
    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
