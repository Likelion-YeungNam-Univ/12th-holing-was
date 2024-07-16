package com.example.atm.bounded_context.auth.service;

import com.example.atm.bounded_context.auth.config.KakaoProperties;
import com.example.atm.bounded_context.auth.dto.TokenInfoDto;
import com.example.atm.bounded_context.auth.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService{

    private final KakaoProperties kakaoProperties;

    /**
     * 인가 코드 요청 uri 생성 : 인가 코드를 요청하는 uri 를 생성하여 반환합니다.<br>
     * @return uri
     */
    @Override
    public String getAuthorizeUri() {
        return UriComponentsBuilder.fromHttpUrl(kakaoProperties.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .queryParam("scope", kakaoProperties.getScope())
                .toUriString();
    }

    /**
     * 토큰 받기 : 인가 코드로 토큰 발급을 요청합니다.<br>
     * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
     * @param code 인가 코드
     * @return TokenInfoDto 발급된 토큰
     * @throws HttpClientErrorException api 요청 실패 시 발생합니다.
     */
    @Override
    public TokenInfoDto getToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("client_secret", kakaoProperties.getClientSecret());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoProperties.getTokenUri(), entity, String.class);

        JSONObject tokenInfoJson = new JSONObject(response.getBody());
        String accessToken = tokenInfoJson.getString("access_token");
        String refreshToken = tokenInfoJson.getString("refresh_token");
        String scope = tokenInfoJson.getString("scope");

        return TokenInfoDto.of(accessToken, refreshToken, scope);
    }

    @Override
    public UserInfoDto getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public void unlink(String socialId) {

    }
}
