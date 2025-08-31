package com.ptit.event_management.configurations;

import com.ptit.event_management.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.secretKey}")
    private String secretKey;

    private static String SECRET_KEY;

    @PostConstruct
    public void init() {
        SECRET_KEY = secretKey;
    }

    public static SecretKey getKey() {
            byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    public static String generateToken(Authentication auth) {
        try {
            User userDetails = (User) auth.getPrincipal();

            return Jwts.builder()
                    .signWith(getKey())
                    .claim("email", userDetails.getEmail())
                    .claim("userId", userDetails.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                    .compact();
        } catch (Exception e) {
            throw new InvalidParameterException("Cannot create jwt token, error = " + e.getMessage());
        }
    }

    public static Claims extractClaims(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalArgumentException("JWT token is missing");
        }
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(jwt).getBody();
    }

    public static String getEmailFromToken(String jwt) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }
        return extractClaims(jwt).get("email").toString();
    }

    public static Integer getUserIdFromToken(String jwt) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }
        return (Integer) extractClaims(jwt).get("user-id");
    }

    public <T> T getClaimFromToken(String jwt, String key, Class<T> clazz) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        Object claim = extractClaims(jwt).get(key);

        return clazz.cast(claim);
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        try {
            String email = getEmailFromToken(jwt);
            Date expirationDate = extractClaims(jwt).getExpiration();

            return email.equals(userDetails.getUsername()) && expirationDate.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
