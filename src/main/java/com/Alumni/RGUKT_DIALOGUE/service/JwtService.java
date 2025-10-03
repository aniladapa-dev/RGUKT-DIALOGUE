package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Service to handle JWT creation, parsing, and validation
 * Automatically generates a 512-bit HS256 key on startup
 */
@Service
public class JwtService {

    // Automatically generated key (HMAC SHA-256 512-bit)
    private final Key SIGNING_KEY;

    public JwtService() {
        // Generate key programmatically once
        SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println("Generated JWT Secret Key: " + SIGNING_KEY); // Optional: for debugging
    }

    private Key getSignInKey() {
        return SIGNING_KEY;
    }

    /**
     * Generate JWT token for a user
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h expiry
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = extractClaims(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    public boolean validateToken(String token, User user) {
        if(user == null) return false;
        final String email = extractUserName(token);
        return (email.equals(user.getEmail())) && !isTokenExpired(token);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
