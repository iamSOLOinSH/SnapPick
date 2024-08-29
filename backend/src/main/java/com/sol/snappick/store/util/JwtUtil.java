package com.sol.snappick.store.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

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
	 * @param storeUuid        사용자 식별자 (예: store UUID)
	 * @param expirationMillis 토큰 만료 시간 (밀리초)
	 * @return 생성된 JWT 토큰
	 */
	public String generateToken(
			String storeUuid,
			long expirationMillis
	) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationMillis);

		return Jwts.builder()
				   .setSubject(storeUuid)
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
		} catch(Exception e) {
			log.info("Invalid JWT token: " + e.getMessage());
			return false;
		}
	}

	/**
	 * JWT 토큰에서 클레임을 추출하는 메서드
	 *
	 * @param token JWT 토큰
	 * @return JWT 클레임 (예: storeUuid)
	 */
	public String getStoreUUIDFromToken(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(key)
							.build()
							.parseClaimsJws(token)
							.getBody();
		return claims.getSubject();  // storeId는 JWT의 subject로 저장되었다고 가정합니다.
	}

	public Key getSecretKey() {
		return key;
	}
}
