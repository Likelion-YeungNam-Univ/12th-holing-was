package com.example.holing.bounded_context.auth.dto;

public record OAuthUserInfoDto(
        Long id,
        String nickname,
        String email,
        String profileImageUrl
) {
    public static OAuthUserInfoDto of(Long id, String nickname, String email, String profileImageUrl) {
        return new OAuthUserInfoDto(id, nickname, email, profileImageUrl);
    }
}
