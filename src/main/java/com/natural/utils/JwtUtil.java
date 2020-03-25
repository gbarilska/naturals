package com.natural.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class JwtUtil {

    private final static String secretKey = "secret";
    private final static Long expiration = 8L;

    private JwtUtil() {
        throw new AssertionError();
    }


    public static String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static LocalDate extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDate.now());
    }

    public static String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(expiration)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}