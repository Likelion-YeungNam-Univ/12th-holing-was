package com.example.atm.bounded_context.auth.dto;

public record OAuthTokenInfoDto(
        String accessToken,
        String refreshToken,
        String scope
) {
    public static OAuthTokenInfoDto of(String accessToken, String refreshToken, String scope) {
        return new OAuthTokenInfoDto(accessToken, refreshToken, scope);
    }
}
