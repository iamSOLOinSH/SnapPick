package com.sol.snappick.member.auth;

import com.sol.snappick.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtConfig jwtConfig;


    // JWT 생성
    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return generateTokenHelper(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    private String generateTokenHelper(Date expiry, Member member) {
        Date now = new Date();

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .issuer(jwtConfig.getIssuer())
                .issuedAt(now)
                .expiration(expiry) // 만료시간
                .subject(member.getId().toString())
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    // 유효성 검증
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 인증정보를 담은 Authentication 객체 반환
    public Authentication getAuthentication(String token) {

        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(),
                        "", authorities), token, authorities
        );
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
