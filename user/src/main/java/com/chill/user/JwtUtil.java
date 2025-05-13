package com.chill.user;

import com.chill.user.dto.DecodedTokenDTO;
import com.chill.user.models.UserModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Component
public class JwtUtil {

    // Inject from application.properties instead of hard-coding:
    @Value("${jwt.secret}")
    private String base64Secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey key;

    // Lazily initialize the SecretKey once
    private SecretKey getSigningKey() {
        if (key == null) {
            byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
            key = Keys.hmacShaKeyFor(keyBytes);
        }
        return key;
    }

    public String generateToken(UserModel user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public DecodedTokenDTO decodeToken(String token) {
        // If invalid or expired, parseClaimsJws will throw JwtException
        Jws<Claims> parsed = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);

        Claims body = parsed.getBody();
        return new DecodedTokenDTO(
                body.getSubject(),
                body.get("username", String.class),
                body.get("roles", List.class),
                body.getIssuedAt(),
                body.getExpiration()
        );
    }

    public String generateResetToken(UserModel user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}


