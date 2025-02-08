package com.example.backend.config;

import com.example.backend.ENUM.USER_ROLE;
import com.example.backend.serviceImpl.SecretKeyService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Service
public class JwtProvider {
    private SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public String generatedToken(Authentication authentication){

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = populateAuthorities(authorities);

        String jwt = Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_TIME))
                .claim("email", authentication.getName())
                .claim("authorities", role)
                .signWith(secretKey)
                .compact();
        return jwt;
    }
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 giờ
    public String generateAccessToken(String email, USER_ROLE role) {

        return Jwts.builder()
                .claim("email", email)
                .claim("authorities", role)
                .setIssuedAt(new Date()) // Thời gian tạo token
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_TIME)) // Hạn sử dụng
                .signWith(secretKey) // Ký bằng secret key
                .compact(); // Tạo token
    }

    public String getUserNameFromJwtToken(String jwt){
        jwt = jwt.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }

    public Date extractExpiration(String jwt) {
        jwt = jwt.substring(7); // Remove "Bearer " prefix if exists
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getExpiration();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority>authorities) {
        Set<String> auths = new HashSet<>();
        for(GrantedAuthority authority:authorities){
            auths.add(authority.getAuthority());
        }

        return String.join(",", auths);
    }
}
