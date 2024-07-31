package com.example.holing.bounded_context.auth.dto;

public record AccessTokenResponseDto(
        String accessToken
) {
    public static AccessTokenResponseDto of(String accessToken) {
        return new AccessTokenResponseDto(accessToken);
    }
}
