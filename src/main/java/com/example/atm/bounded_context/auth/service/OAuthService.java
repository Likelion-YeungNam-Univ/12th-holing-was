package com.example.atm.bounded_context.auth.service;

import com.example.atm.bounded_context.auth.dto.OAuthTokenInfoDto;
import com.example.atm.bounded_context.auth.dto.OAuthUserInfoDto;

public interface OAuthService {
    String getAuthorizeUri();

    OAuthTokenInfoDto getToken(String code);

    OAuthUserInfoDto getUserInfo(String accessToken);

    void unlink(String socialId);
}