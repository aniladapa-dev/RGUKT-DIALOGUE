package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.model.User;
import com.Alumni.RGUKT_DIALOGUE.model.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Responsible for creating, reading, and validating JWT tokens
 * Handles both User and Admin tokens
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignInKey() {
        return signingKey;
    }

    // ------------------ USER TOKEN ------------------
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, User user) {
        if (user == null) return false;
        final String email = extractUserName(token);
        final String role = extractRole(token);
        return email.equals(user.getEmail()) && role.equals("USER") && !isTokenExpired(token);
    }

    // ------------------ ADMIN TOKEN ------------------
    public String generateToken(Admin admin) {
        return Jwts.builder()
                .setSubject(admin.getEmail())
                .claim("adminId", admin.getId())
                .claim("role", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, Admin admin) {
        if (admin == null) return false;
        final String email = extractUserName(token);
        final String role = extractRole(token);
        return email.equals(admin.getEmail()) && role.equals("ADMIN") && !isTokenExpired(token);
    }

    // ------------------ COMMON METHODS ------------------
    public String extractUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = extractClaims(token);
        return claims.get("userId") != null
                ? Long.valueOf(claims.get("userId").toString())
                : null;
    }

    public Long extractAdminId(String token) {
        Claims claims = extractClaims(token);
        return claims.get("adminId") != null
                ? Long.valueOf(claims.get("adminId").toString())
                : null;
    }

    public String extractRole(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class);
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
