package com.example.atm.bounded_context.auth.service;

import com.example.atm.bounded_context.auth.dto.TokenInfoDto;
import com.example.atm.bounded_context.auth.dto.UserInfoDto;

public interface OAuthService {
    String getAuthorizeUri();

    TokenInfoDto getToken(String code);

    UserInfoDto getUserInfo(String accessToken);

    void unlink(String socialId);
}
