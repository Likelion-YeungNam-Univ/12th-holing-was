package com.example.holing.bounded_context.auth.dto;

import com.example.holing.bounded_context.user.entity.User;

public record SignInResponseDto(
        String accessToken,
        Long socialId,
        Boolean isSelfTested
) {
    public static SignInResponseDto of(String accessToken, User user) {
        return new SignInResponseDto(
                accessToken,
                user.getSocialId(),
                user.getIsSelfTested()
        );
    }
}
