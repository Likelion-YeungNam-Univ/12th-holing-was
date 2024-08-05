package com.example.holing.bounded_context.auth.dto;

public record SignInResponseDto(
        String accessToken,
        Long socialId
) {
    public static SignInResponseDto of(String accessToken, Long socialId) {
        return new SignInResponseDto(accessToken, socialId);
    }
}
