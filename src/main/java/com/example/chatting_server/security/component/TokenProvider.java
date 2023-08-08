package com.example.chatting_server.security.component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;

@Component
public class TokenProvider implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String USER_ID_KEY = "userId";

    private final String secret;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.accessToken-validity-in-seconds}") long accessTokenValidTime,
                         @Value("${jwt.refreshToken-validity-in-seconds}") long refreshTokenValidTime) {
        this.secret = secret;
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * access token 생성 algorithm
     */
    public String createAccessToken(Authentication authentication) {
        String memberId = authentication.getName();

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidTime);

        Map<String, String> claim = new HashMap<>();
        claim.put(USER_ID_KEY, memberId);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claim)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * refresh token 생성 algorithm
     */
    public String createRefreshToken(Authentication authentication) {
        String memberId = authentication.getName();

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidTime);

        Map<String, String> claim = new HashMap<>();
        claim.put(USER_ID_KEY, memberId);

        return Jwts.builder()
                .setClaims(claim)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 인증 정보 조회
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<GrantedAuthority> authorities = Collections.emptyList();
        User principal = new User(claims.get(USER_ID_KEY).toString(), claims.get(USER_ID_KEY).toString(), authorities);

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    /**
     * access token 유효성 검증
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid AccessToken Signature");
        } catch (ExpiredJwtException e) {
            logger.info("Expired AccessToken Token");
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported AccessToken Token");
        } catch (IllegalArgumentException e) {
            logger.info("Invalid AccessToken Token");
        }
        return false;
    }

    /**
     * Refresh Token 유효성 검증
     */
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid RefreshToken Signature");
        } catch (ExpiredJwtException e) {
            logger.info("Expired RefreshToken Token");
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported RefreshToken Token");
        } catch (IllegalArgumentException e) {
            logger.info("Invalid RefreshToken Token");
        }
        return false;
    }

    /**
     * 토큰에서 회원 ID 추출
     */
    public String getMemberIdFromToken(String bearerToken) {
        String token = bearerToken;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get(USER_ID_KEY).toString();
    }
}
