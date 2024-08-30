package com.sol.snappick.store.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 토큰 생성 메서드
     *
     * @param storeId 사용자 식별자 (예: store id)
     * @param minute  토큰 만료 시간 (분)
     * @return 생성된 JWT 토큰
     */
    public String generateToken(
        String storeId,
        Integer minute
    ) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + minute * 60 * 1000);

        return Jwts.builder()
                   .setSubject(storeId)
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(key)
                   .compact();
    }

    /**
     * JWT 토큰 유효성 검증 메서드
     *
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효한지 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    /**
     * JWT 토큰에서 클레임을 추출하는 메서드
     *
     * @param token JWT 토큰
     * @return JWT 클레임 (예: storeId)
     */
    public String getStoreIdFromToken(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }

    public Key getSecretKey() {
        return key;
    }
}
