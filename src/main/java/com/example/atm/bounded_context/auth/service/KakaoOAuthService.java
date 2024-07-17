package com.example.atm.bounded_context.auth.service;

import com.example.atm.bounded_context.auth.config.KakaoProperties;
import com.example.atm.bounded_context.auth.dto.OAuthTokenInfoDto;
import com.example.atm.bounded_context.auth.dto.OAuthUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
public class KakaoOAuthService implements OAuthService {

    private final KakaoProperties kakaoProperties;

    /**
     * 인가 코드 요청 uri 생성 : 인가 코드를 요청하는 uri 를 생성하여 반환합니다.<br>
     *
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
     *
     * @param code 인가 코드
     * @return TokenInfoDto 토큰 정보
     * @throws HttpClientErrorException api 요청 실패 시 발생합니다.
     */
    @Override
    public OAuthTokenInfoDto getToken(String code) {
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
        ResponseEntity<String> response = restTemplate.exchange(kakaoProperties.getTokenUri(), HttpMethod.POST, entity, String.class);

        JSONObject tokenInfoJson = new JSONObject(response.getBody());
        String accessToken = tokenInfoJson.getString("access_token");
        String refreshToken = tokenInfoJson.getString("refresh_token");
        String scope = tokenInfoJson.getString("scope");

        return OAuthTokenInfoDto.of(accessToken, refreshToken, scope);
    }

    /**
     * 사용자 정보 받기 : 현재 로그인한 사용자의 정보를 불러옵니다.<br>
     * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
     *
     * @param accessToken 소셜 인증 서버 access token
     * @return UserInfoDto 유저 정보
     * @throws HttpClientErrorException api 요청 실패 시 발생합니다.
     */
    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(kakaoProperties.getUserInfoUri(), HttpMethod.GET, entity, String.class);

        JSONObject userInfoJson = new JSONObject(response.getBody());
        Long id = userInfoJson.getLong("id");
        String email = userInfoJson.getJSONObject("kakao_account").getString("email");
        String nickname = userInfoJson.getJSONObject("kakao_account").getJSONObject("profile").getString("nickname");
        String profileImageUrl = userInfoJson.getJSONObject("kakao_account").getJSONObject("profile").getString("profile_image_url");

        return OAuthUserInfoDto.of(id, nickname, email, profileImageUrl);
    }

    @Override
    public void unlink(String socialId) {

    }
}
