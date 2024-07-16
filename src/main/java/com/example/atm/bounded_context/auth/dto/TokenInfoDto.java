package com.example.atm.bounded_context.auth.dto;

public record TokenInfoDto(
        String accessToken,
        String refreshToken,
        String scope
){
    public static TokenInfoDto of(String accessToken, String refreshToken, String scope){
        return new TokenInfoDto(accessToken, refreshToken, scope);
    }
}
