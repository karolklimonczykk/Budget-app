package com.example.pasir_klimonczyk_karol.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    // Stabliny klucz, nie zmienia się po każdym uruchomieniu
    private final Key key = Keys.hmacShaKeyFor(
            "mojaMegaBezpiecznaTajnaSekretnaFrazaDoJwtTokenow1234567890ABCDEF!@#".getBytes()
    );

    // Generowanie tokena na postawie użytkownika
    public String generateToken(com.example.pasir_klimonczyk_karol.model.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        long expirationMs = 3600000;                        // Czas ważności tokena: 1h
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())                // dla kompatybilności
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            // jeśli prasowanie się uda - token poprawny
            extractAllClaims(token);
            return true;
        } catch (Exception e){
            // nieprawidłowy token (no. podpis, wygasł, błędny format)
            return false;
        }
    }

}
