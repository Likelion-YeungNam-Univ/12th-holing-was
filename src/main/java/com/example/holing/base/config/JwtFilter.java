package com.example.holing.base.config;

import com.example.holing.base.jwt.JwtProvider;
import com.example.holing.bounded_context.user.entity.User;
import com.example.holing.bounded_context.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtProvider.getToken(request);
            String userId = jwtProvider.getUserId(accessToken);
            User user = userService.read(Long.parseLong(userId));

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, null); //인증객체 생성
            SecurityContextHolder.getContext().setAuthentication(authentication); //인증정보 저장
            filterChain.doFilter(request, response);
        } catch (Exception e) {

        }
    }
}
