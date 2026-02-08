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

    // 🔐 Génération du token
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role) // EMPLOYEE / MANAGER / ADMIN
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                )
                .signWith(key)
                .compact();
    }

    // 📧 Email
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 🎭 Rôle
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ✅ Validation
    public boolean validateToken(String token, String email) {
        return extractUsername(token).equals(email) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
