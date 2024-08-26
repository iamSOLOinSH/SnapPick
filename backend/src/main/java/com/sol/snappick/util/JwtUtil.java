package com.sol.snappick.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 토큰 생성 메서드
     * @param storeUuid
     * @param expirationMillis
     * @return
     */
    public String generateToken(String storeUuid, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                   .setSubject(storeUuid)
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(key)
                   .compact();
    }

    public String validateTokenAndGetStoreId(String token) {
        return Jwts.parser()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }
}
