package com.example.odyssea.security;

import com.example.odyssea.exceptions.JwtToken.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public JwtToken generateToken(String email, int id, String firstName, String lastName) {
        String token = Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return new JwtToken(token);
    }

    public JwtToken generateTokenWithId(Integer id){
        String token = Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return new JwtToken(token);
    }


    public String getIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Integer extractUserId(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Integer.class);
    }

    public void validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Malformed JWT");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenExpiredException("JWT expired");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenUnsupportedException("JWT not supported");
        } catch (SignatureException ex) {
            throw new JwtTokenSignatureException("Invalid JWT signature");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("Empty or invalid JWT content");
        }
    }







}