package com.example.holing.base.jwt;

import com.example.holing.base.exception.GlobalException;
import com.example.holing.bounded_context.auth.exception.AuthExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private final JwtProperties jwtProperties;

    /**
     * JWT(Json Web Token) 생성<br>
     * 유저의 이메일과 아이디를 payload 넣어 JWT 토큰 생성
     *
     * @param email  유저 이메일
     * @param userId 유저 아이디
     * @return access token(클라이언트 서버)
     */
    public String generatorAccessToken(String email, Long userId) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(email)
                .setIssuer(jwtProperties.getIssuer())
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey().getBytes())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getExpiration()))
                .compact();
    }

    /**
     * 헤더 인증 필드 검사 : 헤더의 인증 필드에 있는 토큰 반환
     *
     * @param request
     * @return parseToken() Bearer 토큰 검사
     * @throws IllegalArgumentException 헤더의 인증 필드에 토큰이 존재하지 않는 경우 발생합니다.
     */
    public String getToken(HttpServletRequest request) {
        if (request.getHeader(AUTH_HEADER) == null)
            throw new GlobalException(AuthExceptionCode.EMPTY_AUTHORIZATION);
        return parseToken(request.getHeader(AUTH_HEADER));
    }

    /**
     * Bearer 토큰 검사 : "Bearer "를 제외한 토큰 값 반환
     *
     * @param token
     * @return access token(클라이언트 서버)
     * @throws IllegalArgumentException 인증 필드의 값에 "Bearer "가 존재하지 않는 경우 발생합니다.
     */
    public String parseToken(String token) {
        if (!token.startsWith(BEARER))
            throw new GlobalException(AuthExceptionCode.MISMATCH_TOKEN_TYPE);
        return token.substring(BEARER.length());
    }

    /**
     * access token 유효성 검사 : access token(JWT) 의 payload 반환
     *
     * @param token
     * @return access token payload(클라이언트 서버)
     * @throws SignatureException    서명이 맞지 않는 경우 발생합니다.
     * @throws MalformedJwtException 형식이 맞지 않는 경우 발생합니다.
     * @throws ExpiredJwtException   만료된 경우 발생합니다.
     */
    public Claims getValidToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey().getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        return getValidToken(token).get("userId").toString();
    }
}
