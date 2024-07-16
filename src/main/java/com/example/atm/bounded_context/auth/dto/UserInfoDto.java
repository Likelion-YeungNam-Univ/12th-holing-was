package com.example.atm.bounded_context.auth.dto;

public record UserInfoDto(
        Long id,
        String nickname,
        String email,
        String profileImageUrl
) {
    public static UserInfoDto of(Long id, String nickname, String email, String profileImageUrl) {
        return new UserInfoDto(id, nickname, email, profileImageUrl);
    }
}
