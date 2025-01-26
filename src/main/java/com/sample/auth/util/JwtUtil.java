package com.sample.auth.util;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String secret = "Team32";

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String userRole) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, userRole);
    }

    private String createToken(Map<String, Object> claims, String subject, String userRole) {
        claims.put("role", userRole);
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }

    //    public String extractUserRole(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.get("role", String.class);
//    }
// Method to extract the username from the token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject(); // Get the subject, which is the username
    }

    // Method to extract the role from the token
    public String extractUserRole(String token) {
        return extractAllClaims(token).get("role", String.class); // Get the role from claims
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token); // This will throw an exception if the token is invalid
            return !isTokenExpired(token); // Ensure the token is not expired
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.out.println("Invalid token signature: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }
}