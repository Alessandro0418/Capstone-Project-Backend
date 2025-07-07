package it.epicode.Capstone_Project_Backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import it.epicode.Capstone_Project_Backend.exeption.TokenExpiredExeption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String generateToken(String username, String roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRoles(String token) {
        return parseClaims(token).get("roles", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        }  catch (ExpiredJwtException e) {
            throw new TokenExpiredExeption("Token scaduto");
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}